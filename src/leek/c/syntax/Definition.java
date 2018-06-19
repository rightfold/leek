package leek.c.syntax;

import java.util.List;

import leek.c.analysis.AnalysisException;
import leek.c.analysis.GlobalScope;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.ClassWriter;

/**
 * Base class for syntax elements that appear at the top-levels of source
 * files.
 */
public abstract class Definition extends Node
{
    public Definition(SourceLocation sourceLocation)
    {
        super(sourceLocation);
    }

    /**
     * Define this definition in the global scope.
     */
    public abstract void define(GlobalScope gs) throws AnalysisException;

    /**
     * Type check this definition and generate code for it.
     *
     * @param classes A list of classes onto which to append generated classes.
     */
    public abstract void analyze(GlobalScope gs, List<ClassWriter> classes)
        throws AnalysisException;
}
