package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.MethodVisitor;
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
    public boolean equalsType(Type other)
    {
        if (other instanceof ArrayType)
        {
            ArrayType otherArray = (ArrayType)other;
            return elementType.equalsType(otherArray.elementType);
        }
        return false;
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
    public int primitiveType()
    {
        return -1;
    }

    @Override
    public String className()
    {
        return "[" + elementType.descriptor();
    }

    @Override
    public void defaultValue(MethodVisitor mv)
    {
        mv.visitInsn(Opcodes.ICONST_0);

        int newarrayOperand = elementType.primitiveType();
        if (newarrayOperand == -1)
        {
            mv.visitTypeInsn(Opcodes.ANEWARRAY, elementType.className());
        }
        else
        {
            mv.visitIntInsn(Opcodes.NEWARRAY, newarrayOperand);
        }
    }

    @Override
    public int storeOpcode()
    {
        return Opcodes.ASTORE;
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
