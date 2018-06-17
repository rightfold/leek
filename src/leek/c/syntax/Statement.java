package leek.c.syntax;

import leek.c.analysis.AnalysisException;
import leek.c.analysis.LocalScope;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.MethodVisitor;

/**
 * Class for statements.
 */
public abstract class Statement extends Node
{
    public Statement(SourceLocation sourceLocation)
    {
        super(sourceLocation);
    }

    /**
     * Type check this expression and generate code for it.
     */
    public abstract void analyze(MethodVisitor mv, LocalScope ls)
         throws AnalysisException;
}
