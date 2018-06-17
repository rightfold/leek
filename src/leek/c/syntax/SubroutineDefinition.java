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
        writeClinitMethod(cw);
        writeInvokeMethod(cw);
        cw.visitEnd();
        classes.add(cw);
    }

    private LocalScope createBodyLocalScope() throws AnalysisException
    {
        // Skip the receiver, so start at one.
        int slot = 1;

        LocalScope scope = new LocalScope();

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
            /* name */ "instance",
            /* descriptor */ classDescriptor()
        );
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private MethodVisitor writeClinitMethodMetadata(ClassVisitor cv)
    {
        return cv.visitMethod(
            Opcodes.ACC_PRIVATE,
            /* name */ "<clinit>",
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

        // Initialize result variable.
        returnType.defaultValue(mv);
        mv.visitVarInsn(returnType.storeOpcode(), resultSlot);

        // Analyze body.
        for (Statement statement : body)
        {
            statement.analyze(mv, scope);
        }

        // Return value of result variable.
        mv.visitVarInsn(returnType.loadOpcode(), resultSlot);
        mv.visitInsn(returnType.returnOpcode());

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
        String parameterDescriptors =
            parameters.stream()
            .map(p -> p.type.descriptor())
            .collect(Collectors.joining());
        String returnTypeDescriptor = returnType.descriptor();
        return "(" + parameterDescriptors + ")" + returnTypeDescriptor;
    }
}
