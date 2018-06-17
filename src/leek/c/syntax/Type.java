package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.MethodVisitor;

public abstract class Type extends Node
{
    public Type(SourceLocation sourceLocation)
    {
        super(sourceLocation);
    }

    /**
     * Whether this type is the same type as another type.
     */
    public abstract boolean equalsType(Type other);

    /**
     * Return the JVM type descriptor for this type.
     */
    public abstract String descriptor();

    /**
     * Return the JVM type descriptor for this type when boxed.
     */
    public abstract String boxedDescriptor();

    /**
     * The primitive type as a member of Opcodes, or -1 if not a primitive
     * type.
     */
    public abstract int primitiveType();

    /**
     * The name of the class, or null if this is a primitive type.
     */
    public abstract String className();

    /**
     * Generate the default value for this type.
     */
    public abstract void defaultValue(MethodVisitor mv);

    /**
     * The load opcode for this type, e.g. ASTORE or ISTORE.
     */
    public abstract int storeOpcode();

    /**
     * The load opcode for this type, e.g. ALOAD or ILOAD.
     */
    public abstract int loadOpcode();

    /**
     * The return opcode for this type, e.g. ARETURN or IRETURN.
     */
    public abstract int returnOpcode();
}
