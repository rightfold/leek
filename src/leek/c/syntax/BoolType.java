package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.Opcodes;

public final class BoolType extends Type
{
    public BoolType(SourceLocation sourceLocation)
    {
        super(sourceLocation);
    }

    @Override
    public String descriptor()
    {
        return "Z";
    }

    @Override
    public String boxedDescriptor()
    {
        return "Ljava/lang/Boolean;";
    }

    @Override
    public int loadOpcode()
    {
        return Opcodes.ILOAD;
    }

    @Override
    public int returnOpcode()
    {
        return Opcodes.IRETURN;
    }
}
