package leek.c.syntax;

import leek.c.analysis.AnalysisException;
import leek.c.analysis.LocalScope;
import leek.c.analysis.Variable;

import leek.c.diagnostics.SourceLocation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Variable expressions evaluate to a value in scope.
 */
public final class VariableExpression extends Expression
{
    public final String name;

    public VariableExpression(SourceLocation sourceLocation, String name)
    {
        super(sourceLocation);
        this.name = name;
    }

    @Override
    public Type analyze(MethodVisitor mv, LocalScope ls)
         throws AnalysisException
    {
        Variable variable = ls.getVariable(name);
        mv.visitVarInsn(variable.type.loadOpcode(), variable.slot);
        return variable.type;
    }
}
