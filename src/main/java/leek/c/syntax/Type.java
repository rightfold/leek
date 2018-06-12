package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

public abstract class Type extends Node
{
    public Type(SourceLocation sourceLocation)
    {
        super(sourceLocation);
    }
}
