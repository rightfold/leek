package leek.c.analysis;

import java.util.HashMap;
import java.util.Map;

public final class LocalScope
{
    /**
     * The parent scope. May be null, in case this is the uppermost local
     * scope.
     */
    public final LocalScope parent;

    private final Map<String, Variable> variables;

    public LocalScope(LocalScope parent)
    {
        this.parent = parent;
        this.variables = new HashMap<>();
    }

    public Variable getVariable(String name) throws AnalysisException
    {
        Variable variable = this.variables.get(name);
        if (variable != null)
        {
            return variable;
        }
        else if (parent != null)
        {
            return parent.getVariable(name);
        }
        else
        {
            // TODO(foldr): Throw more informational exception.
            throw new AnalysisException();
        }
    }
}
