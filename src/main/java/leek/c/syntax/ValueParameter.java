package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

/**
 * Value parameters on subroutines.
 */
public final class ValueParameter extends Node
{
    public final String name;
    public final Type type;

    public ValueParameter(
        SourceLocation sourceLocation,
        String name,
        Type type
    )
    {
        super(sourceLocation);
        this.name = name;
        this.type = type;
    }
}
