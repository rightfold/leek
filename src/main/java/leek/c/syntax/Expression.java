package leek.c.syntax;

import leek.c.analysis.AnalysisException;
import leek.c.analysis.LocalScope;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.MethodVisitor;

/**
 * Class for expressions. Expressions can be evaluated, yielding values.
 */
public abstract class Expression extends Node
{
    public Expression(SourceLocation sourceLocation)
    {
        super(sourceLocation);
    }

    /**
     * Type check this expression and generate code for it.
     *
     * @return The type of the expression in the given scope.
     */
    public abstract Type analyze(MethodVisitor mv, LocalScope ls)
         throws AnalysisException;
}
