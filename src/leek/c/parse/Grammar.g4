grammar Grammar;

@header
{
    import java.nio.file.Path;
    import java.nio.file.Paths;

    import leek.c.diagnostics.SourceLocation;

    import leek.c.syntax.BoolType;
    import leek.c.syntax.CopyStatement;
    import leek.c.syntax.Definition;
    import leek.c.syntax.EffectStatement;
    import leek.c.syntax.Expression;
    import leek.c.syntax.Statement;
    import leek.c.syntax.SubroutineDefinition;
    import leek.c.syntax.Type;
    import leek.c.syntax.ValueParameter;
    import leek.c.syntax.VariableExpression;
}

@members
{
    private SourceLocation makeSourceLocation()
    {
        Path path = Paths.get("example");
        return new SourceLocation(path, 1, 1);
    }
}

translationUnit returns [List<Definition> r]
    : definitionList { $r = $definitionList.r; }
    ;

definitionList returns [List<Definition> r]
    @init { $r = new ArrayList<>(); }
    :
    | definition definitionList
        {
            $r.add($definition.r);
            $r.addAll($definitionList.r);
        }
    ;

definition returns [Definition r]
    : subroutineDefinition { $r = $subroutineDefinition.r; }
    ;

subroutineDefinition returns [SubroutineDefinition r]
    :
        kind=subroutineKind
        name=IDENTIFIER
        '('
            parameters=valueParameterList
        ')'
        returnType=type
        'begin'
            body=statementList
        'end'
        {
            SourceLocation sl = makeSourceLocation();
            $r = new SubroutineDefinition(
                sl, $name.text, $kind.r, $parameters.r, $returnType.r, $body.r
            );
        }
    ;

subroutineKind returns [SubroutineDefinition.SubroutineKind r]
    : 'function' { $r = SubroutineDefinition.SubroutineKind.FUNCTION; }
    | 'procedure' { $r = SubroutineDefinition.SubroutineKind.PROCEDURE; }
    ;

valueParameterList returns [List<ValueParameter> r]
    @init { $r = new ArrayList<>(); }
    :
    | valueParameter
        { $r.add($valueParameter.r); }
    | valueParameter ',' valueParameterList
        {
            $r.add($valueParameter.r);
            $r.addAll($valueParameterList.r);
        }
    ;

valueParameter returns [ValueParameter r]
    : name=IDENTIFIER type
        {
            SourceLocation sl = makeSourceLocation();
            $r = new ValueParameter(sl, $name.text, $type.r);
        }
    ;

statementList returns [List<Statement> r]
    @init { $r = new ArrayList<>(); }
    :
    | statement statementList
        {
            $r.add($statement.r);
            $r.addAll($statementList.r);
        }
    ;

statement returns [Statement r]
    : copyStatement { $r = $copyStatement.r; }
    | effectStatement { $r = $effectStatement.r; }
    ;

copyStatement returns [CopyStatement r]
    : 'copy' source=expression 'to' target=IDENTIFIER
        {
            SourceLocation sl = makeSourceLocation();
            $r = new CopyStatement(sl, $source.r, $target.text);
        }
    ;

effectStatement returns [EffectStatement r]
    : 'effect' expression
        {
            SourceLocation sl = makeSourceLocation();
            $r = new EffectStatement(sl, $expression.r);
        }
    ;

expression returns [Expression r]
    : variableExpression { $r = $variableExpression.r; }
    ;

variableExpression returns [VariableExpression r]
    : name=IDENTIFIER
        {
            SourceLocation sl = makeSourceLocation();
            $r = new VariableExpression(sl, $name.text);
        }
    ;

type returns [Type r]
    : boolType { $r = $boolType.r; }
    ;

boolType returns [BoolType r]
    : 'bool'
        {
            SourceLocation sl = makeSourceLocation();
            $r = new BoolType(sl);
        }
    ;

IDENTIFIER : name=[a-zA-Z][a-zA-Z0-9]* ;

WS : [ \n] -> skip ;
