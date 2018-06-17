package leek.c.analysis;

import java.util.HashMap;
import java.util.Map;

public final class LocalScope
{
    private final Map<String, Variable> variables;

    public LocalScope()
    {
        this.variables = new HashMap<>();
    }

    public void defineVariable(String name, Variable variable)
        throws AnalysisException
    {
        if (this.variables.containsKey(name))
        {
            // TODO(foldr): Throw more informational exception.
            throw new AnalysisException();
        }
        this.variables.put(name, variable);
    }

    public Variable getVariable(String name) throws AnalysisException
    {
        Variable variable = this.variables.get(name);
        if (variable != null)
        {
            return variable;
        }
        else
        {
            // TODO(foldr): Throw more informational exception.
            throw new AnalysisException();
        }
    }

    /**
     * The slot of the tracer.
     */
    public int tracerSlot() throws AnalysisException
    {
        return 1;
    }

    /**
     * The slot of the result variable.
     *
     * @throws AnalysisException If there is no result variable.
     */
    public int resultSlot() throws AnalysisException
    {
        Variable resultVariable = getVariable("result");
        return resultVariable.slot;
    }
}
