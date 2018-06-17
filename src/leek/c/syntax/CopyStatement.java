package leek.c.syntax;

import leek.c.analysis.AnalysisException;
import leek.c.analysis.LocalScope;
import leek.c.analysis.Variable;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.MethodVisitor;

/**
 * Copy statements copy values into variables.
 */
public final class CopyStatement extends Statement
{
    public final Expression source;
    public final String target;

    public CopyStatement(
        SourceLocation sourceLocation,
        Expression source,
        String target
    )
    {
        super(sourceLocation);
        this.source = source;
        this.target = target;
    }

    @Override
    public void analyze(MethodVisitor mv, LocalScope ls)
         throws AnalysisException
    {
        Type valueType = source.analyze(mv, ls);
        Variable targetVariable = ls.getVariable(target);
        if (!valueType.equalsType(targetVariable.type))
        {
            // TODO(foldr): Throw more informational exception.
            throw new AnalysisException();
        }
        mv.visitVarInsn(targetVariable.type.storeOpcode(), targetVariable.slot);
    }
}
