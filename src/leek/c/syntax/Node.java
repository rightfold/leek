package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

/**
 * Base class for syntax elements.
 */
public abstract class Node
{
    public final SourceLocation sourceLocation;

    public Node(SourceLocation sourceLocation)
    {
        this.sourceLocation = sourceLocation;
    }
}
