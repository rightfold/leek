package leek.c.analysis;

import java.util.HashMap;
import java.util.Map;

public final class GlobalScope
{
    private final Map<String, Global> globals;

    public GlobalScope()
    {
        this.globals = new HashMap<>();
    }

    public void defineGlobal(String name, Global global)
        throws AnalysisException
    {
        if (globals.containsKey(name))
        {
            // TODO(foldr): Throw more informational exception.
            throw new AnalysisException();
        }
        globals.put(name, global);
    }

    public Global getGlobal(String name) throws AnalysisException
    {
        Global global = globals.get(name);
        if (global != null)
        {
            return global;
        }
        else
        {
            // TODO(foldr): Throw more informational exception.
            throw new AnalysisException();
        }
    }
}
