package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

/**
 * Class for expressions. Expressions can be evaluated, yielding values.
 */
public abstract class Expression extends Node
{
    public Expression(SourceLocation sourceLocation)
    {
        super(sourceLocation);
    }
}
