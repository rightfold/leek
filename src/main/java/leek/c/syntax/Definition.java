package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

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
}
