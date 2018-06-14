package leek.c.syntax;

import java.util.List;

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
        Function,
        Procedure,
    }

    public final String name;
    public final SubroutineKind kind;
    public final List<ValueParameter> parameters;
    public final Type returnType;
    public final Expression body;

    public SubroutineDefinition(
        SourceLocation sourceLocation,
        String name,
        SubroutineKind kind,
        List<ValueParameter> parameters,
        Type returnType,
        Expression body
    )
    {
        super(sourceLocation);
        this.name = name;
        this.kind = kind;
        this.parameters = parameters;
        this.returnType = returnType;
        this.body = body;
    }

    public void analyze(List<ClassVisitor> classes) throws AnalysisException
    {
        int cwFlags = ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS;
        ClassVisitor cv = new ClassWriter(cwFlags);

        writeClassMetadata(cv);

        MethodVisitor mv = writeInvokeMethodMetadata(cv);
        mv.visitCode();
        LocalScope scope = createBodyLocalScope();
        body.analyze(mv, scope);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cv.visitEnd();
        classes.add(cv);
    }

    private LocalScope createBodyLocalScope() throws AnalysisException
    {
        LocalScope scope = new LocalScope(/* parent */ null);
        for (int i = 0; i < parameters.size(); ++i)
        {
            ValueParameter parameter = parameters.get(i);
            Variable variable = new Variable(parameter.type, /* slot */ i);
            scope.defineVariable(parameter.name, variable);
        }
        return scope;
    }

    private void writeClassMetadata(ClassVisitor cv)
    {
        cv.visit(
            Opcodes.V1_7,
            Opcodes.ACC_PUBLIC,
            name,
            /* signature */ null,
            /* superName */ "java/lang/Object",
            /* interfaces */ null
        );
    }

    private MethodVisitor writeInvokeMethodMetadata(ClassVisitor cv)
    {
        return cv.visitMethod(
            Opcodes.ACC_PUBLIC,
            /* name */ "invoke",
            /* descriptor */ "()Ljava/lang/Object;",
            /* signature */ null,
            /* exceptions */ null
        );
    }
}
