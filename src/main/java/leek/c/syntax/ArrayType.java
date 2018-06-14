package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.Opcodes;

public final class ArrayType extends Type
{
    public final Type elementType;

    public ArrayType(SourceLocation sourceLocation, Type elementType)
    {
        super(sourceLocation);
        this.elementType = elementType;
    }

    @Override
    public String descriptor()
    {
        return "[" + elementType.descriptor();
    }

    @Override
    public String boxedDescriptor()
    {
        return descriptor();
    }

    @Override
    public int loadOpcode()
    {
        return Opcodes.ALOAD;
    }

    @Override
    public int returnOpcode()
    {
        return Opcodes.ARETURN;
    }
}
