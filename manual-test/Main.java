import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import leek.rt.TextualTracer;
import leek.rt.Tracer;

public final class Main
{
    private Main()
    {
    }

    public static void main(String[] args) throws IOException
    {
        Writer writer = new OutputStreamWriter(System.out);
        Tracer tracer = new TextualTracer(writer);
        boolean result = example.INSTANCE.invoke(tracer, false, false, false);
        writer.flush();
    }
}
