import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import leek.rt.TextualTracer;
import leek.rt.Tracer;

public final class Main
{
    private Main()
    {
    }

    public static void main(String[] args) throws IOException
    {
        Charset charset = StandardCharsets.UTF_8;
        Writer writer = new OutputStreamWriter(System.out, charset);
        Tracer tracer = new TextualTracer(writer);
        boolean result = example.INSTANCE.invoke(tracer, false, false, false);
        writer.flush();
    }
}
