package leek.c.syntax;

import java.util.List;

import leek.c.diagnostics.SourceLocation;

/**
 * Definitions of subroutines.
 */
public final class SubroutineDefinition extends Definition
{
    /**
     * Functions and procedures are functionally equivalent, except that
     * functions are not traced whilst procedures are.
     */
    public static enum SubroutineKind
    {
        Function,
        Procedure,
    }

    public final String name;
    public final SubroutineKind kind;
    public final List<ValueParameter> parameters;
    public final Type returnType;
    public final Expression body;

    public SubroutineDefinition(
        SourceLocation sourceLocation,
        String name,
        SubroutineKind kind,
        List<ValueParameter> parameters,
        Type returnType,
        Expression body
    )
    {
        super(sourceLocation);
        this.name = name;
        this.kind = kind;
        this.parameters = parameters;
        this.returnType = returnType;
        this.body = body;
    }
}
