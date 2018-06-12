package leek.c.syntax;

import leek.c.diagnostics.SourceLocation;

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
}
