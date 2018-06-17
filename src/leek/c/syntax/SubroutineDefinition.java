package leek.c.syntax;

import java.util.List;
import java.util.stream.Collectors;

import leek.c.analysis.AnalysisException;
import leek.c.analysis.LocalScope;
import leek.c.analysis.Variable;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Definitions of subroutines.
 */
public final class SubroutineDefinition extends Definition
{
    /**
     * Functions and procedures are functionally equivalent, except that
     * functions are not traced whilst procedures are.
     */
    public static enum SubroutineKind
    {
        FUNCTION,
        PROCEDURE,
    }

    public final String name;
    public final SubroutineKind kind;
    public final List<ValueParameter> parameters;
    public final Type returnType;
    public final List<Statement> body;

    public SubroutineDefinition(
        SourceLocation sourceLocation,
        String name,
        SubroutineKind kind,
        List<ValueParameter> parameters,
        Type returnType,
        List<Statement> body
    )
    {
        super(sourceLocation);
        this.name = name;
        this.kind = kind;
        this.parameters = parameters;
        this.returnType = returnType;
        this.body = body;
    }

    public void analyze(List<ClassWriter> classes) throws AnalysisException
    {
        int cwFlags = ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS;
        ClassWriter cw = new ClassWriter(cwFlags);
        writeClassMetadata(cw);
        writeInstanceField(cw);
        writeClinitMethod(cw);
        writeInitMethod(cw);
        writeInvokeMethod(cw);
        cw.visitEnd();
        classes.add(cw);
    }

    private LocalScope createBodyLocalScope() throws AnalysisException
    {
        LocalScope scope = new LocalScope();

        // Skip the receiver and the tracer, so start at two.
        int slot = 2;

        for (ValueParameter parameter : parameters)
        {
            Variable variable = new Variable(parameter.type, slot++);
            scope.defineVariable(parameter.name, variable);
        }

        Variable resultVariable = new Variable(returnType, slot++);
        scope.defineVariable("result", resultVariable);

        return scope;
    }

    private String classDescriptor()
    {
        return "L" + name + ";";
    }

    private void writeClassMetadata(ClassVisitor cv)
    {
        cv.visit(
            Opcodes.V1_7,
            Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
            name,
            /* signature */ null,
            /* superName */ "java/lang/Object",
            /* interfaces */ null
        );
    }

    private void writeInstanceField(ClassVisitor cv)
    {
        cv.visitField(
            Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL,
            /* name */ "INSTANCE",
            classDescriptor(),
            /* signature */ null,
            /* value */ null
        );
    }

    private void writeClinitMethod(ClassVisitor cv)
    {
        MethodVisitor mv = writeClinitMethodMetadata(cv);
        mv.visitCode();
        mv.visitTypeInsn(Opcodes.NEW, name);
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            /* owner */ name,
            /* name */ "<init>",
            /* descriptor */ "()V",
            /* isInterface */ false
        );
        mv.visitFieldInsn(
            Opcodes.PUTSTATIC,
            /* owner */ name,
            /* name */ "INSTANCE",
            /* descriptor */ classDescriptor()
        );
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private MethodVisitor writeClinitMethodMetadata(ClassVisitor cv)
    {
        return cv.visitMethod(
            Opcodes.ACC_STATIC,
            /* name */ "<clinit>",
            /* descriptor */ "()V",
            /* signature */ null,
            /* exceptions */ null
        );
    }

    private void writeInitMethod(ClassVisitor cv)
    {
        MethodVisitor mv = writeInitMethodMetadata(cv);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            /* owner */ "java/lang/Object",
            /* name */ "<init>",
            /* descriptor */ "()V",
            /* isInterface */ false
        );
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private MethodVisitor writeInitMethodMetadata(ClassVisitor cv)
    {
        return cv.visitMethod(
            Opcodes.ACC_PRIVATE,
            /* name */ "<init>",
            /* descriptor */ "()V",
            /* signature */ null,
            /* exceptions */ null
        );
    }

    private void writeInvokeMethod(ClassVisitor cv) throws AnalysisException
    {
        MethodVisitor mv = writeInvokeMethodMetadata(cv);
        mv.visitCode();

        LocalScope scope = createBodyLocalScope();
        int resultSlot = scope.resultSlot();

        writeInvokePrologue(mv, scope);
        writeInvokeBody(mv, scope);
        writeInvokeEpilogue(mv, scope);

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private MethodVisitor writeInvokeMethodMetadata(ClassVisitor cv)
    {
        return cv.visitMethod(
            Opcodes.ACC_PUBLIC,
            /* name */ "invoke",
            invokeMethodDescriptor(),
            /* signature */ null,
            /* exceptions */ null
        );
    }

    private String invokeMethodDescriptor()
    {
        String tracerD = "Lleek/rt/Tracer;";
        String parameterDs =
            parameters.stream()
            .map(p -> p.type.descriptor())
            .collect(Collectors.joining());
        String returnTypeD = returnType.descriptor();
        return "(" + tracerD + parameterDs + ")" + returnTypeD;
    }

    private void writeInvokePrologue(MethodVisitor mv, LocalScope scope)
        throws AnalysisException
    {
        // Tell the tracer the subroutine has been entered.
        if (kind == SubroutineKind.PROCEDURE)
        {
            mv.visitVarInsn(Opcodes.ALOAD, scope.tracerSlot());
            mv.visitLdcInsn(name);
            mv.visitMethodInsn(
                Opcodes.INVOKEINTERFACE,
                /* owner */ "leek/rt/Tracer",
                /* name */ "enter",
                /* descriptor */ "(Ljava/lang/String;)V",
                /* isInterface */ true
            );
        }

        // Initialize the result variable.
        returnType.defaultValue(mv);
        mv.visitVarInsn(returnType.storeOpcode(), scope.resultSlot());
    }

    private void writeInvokeBody(MethodVisitor mv, LocalScope scope)
        throws AnalysisException
    {
        for (Statement statement : body)
        {
            statement.analyze(mv, scope);
        }
    }

    private void writeInvokeEpilogue(MethodVisitor mv, LocalScope scope)
        throws AnalysisException
    {
        // Tell the tracer the subroutine is leaving.
        if (kind == SubroutineKind.PROCEDURE)
        {
            mv.visitVarInsn(Opcodes.ALOAD, scope.tracerSlot());
            mv.visitMethodInsn(
                Opcodes.INVOKEINTERFACE,
                /* owner */ "leek/rt/Tracer",
                /* name */ "leave",
                /* descriptor */ "()V",
                /* isInterface */ true
            );
        }

        // Return the value of the result variable.
        mv.visitVarInsn(returnType.loadOpcode(), scope.resultSlot());
        mv.visitInsn(returnType.returnOpcode());
    }
}
