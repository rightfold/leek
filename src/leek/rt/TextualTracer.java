package leek.rt;

import java.io.IOException;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracers that log to a writer in a verbose textual format.
 */
public final class TextualTracer implements Tracer
{
    private final Writer w;
    private List<String> stack;

    public TextualTracer(Writer w)
    {
        this.w = w;
        this.stack = new ArrayList<>();
    }

    public void enter(String callee)
    {
        writeLine("ENTER " + callee);
        this.stack.add(callee);
    }

    public void leave()
    {
        String callee = this.stack.get(this.stack.size() - 1);
        this.stack.remove(this.stack.size() - 1);
        writeLine("LEAVE " + callee);
    }

    private void writeLine(String line)
    {
        try
        {
            int size = stack.size();
            for (int i = 0; i < size; ++i)
            {
                w.write(" ");
            }
            w.write(line);
            w.write("\n");
        }
        catch (IOException e)
        {
        }
    }
}
