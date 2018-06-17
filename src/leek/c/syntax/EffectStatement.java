package leek.c.syntax;

import leek.c.analysis.AnalysisException;
import leek.c.analysis.LocalScope;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Effect statements evaluate expressions and discard their results.
 */
public final class EffectStatement extends Statement
{
    public final Expression expression;

    public EffectStatement(
        SourceLocation sourceLocation,
        Expression expression
    )
    {
        super(sourceLocation);
        this.expression = expression;
    }

    @Override
    public void analyze(MethodVisitor mv, LocalScope ls)
         throws AnalysisException
    {
        expression.analyze(mv, ls);
        mv.visitInsn(Opcodes.POP);
    }
}
