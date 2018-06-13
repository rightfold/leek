package leek.c.syntax;

import java.util.List;

import leek.c.analysis.AnalysisException;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.ClassVisitor;

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
     * Type check this definition and generate code for it.
     *
     * @param classes A list of classes onto which to append generated classes.
     */
    public abstract void analyze(List<ClassVisitor> classes) throws AnalysisException;
}
