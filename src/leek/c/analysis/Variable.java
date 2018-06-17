package leek.c.analysis;

import leek.c.syntax.Type;

/**
 * Information about a variable in scope.
 */
public final class Variable
{
    public final Type type;

    /**
     * The slot on the stack frame during code generation.
     */
    public final int slot;

    public Variable(Type type, int slot)
    {
        this.type = type;
        this.slot = slot;
    }
}
