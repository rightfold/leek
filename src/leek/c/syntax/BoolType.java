package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public final class BoolType extends Type
{
    public BoolType(SourceLocation sourceLocation)
    {
        super(sourceLocation);
    }

    @Override
    public boolean equalsType(Type other)
    {
        return other instanceof BoolType;
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
    public int primitiveType()
    {
        return Opcodes.T_BOOLEAN;
    }

    @Override
    public String className()
    {
        return null;
    }

    @Override
    public void defaultValue(MethodVisitor mv)
    {
        mv.visitInsn(Opcodes.ICONST_0);
    }

    @Override
    public int storeOpcode()
    {
        return Opcodes.ISTORE;
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
