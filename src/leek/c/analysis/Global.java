package leek.c.analysis;

import leek.c.syntax.Type;

/**
 * Information about a global.
 */
public final class Global
{
    public final Type type;

    public Global(Type type)
    {
        this.type = type;
    }
}
