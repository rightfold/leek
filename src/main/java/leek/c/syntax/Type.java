package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

public abstract class Type extends Node
{
    public Type(SourceLocation sourceLocation)
    {
        super(sourceLocation);
    }

    /**
     * Return the JVM type descriptor for this type.
     */
    public abstract String descriptor();

    /**
     * Return the JVM type descriptor for this type when boxed.
     */
    public abstract String boxedDescriptor();

    /**
     * The load opcode for this type, e.g. ALOAD or ILOAD.
     */
    public abstract int loadOpcode();

    /**
     * The return opcode for this type, e.g. ARETURN or IRETURN.
     */
    public abstract int returnOpcode();
}
