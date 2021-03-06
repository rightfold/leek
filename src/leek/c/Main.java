package leek.c;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.List;

import leek.c.analysis.GlobalScope;

import leek.c.parse.GrammarLexer;
import leek.c.parse.GrammarParser;

import leek.c.syntax.Definition;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.TokenStream;

import org.objectweb.asm.ClassWriter;

public final class Main
{
    private Main()
    {
    }

    public static void main(String[] args) throws Exception
    {
        String source =
            "procedure example(a bool, b bool, c bool) bool\n" +
            "    begin\n" +
            "        copy a to result\n" +
            "        copy b to result\n" +
            "        copy c to result\n" +
            "        effect a\n" +
            "        effect b\n" +
            "        effect c\n" +
            "    end\n";

        CharStream input = CharStreams.fromString(source);
        TokenSource lexer = new GrammarLexer(input);
        TokenStream tokens = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokens);

        Definition example = parser.definition().r;

        GlobalScope gs = new GlobalScope();
        List<ClassWriter> classes = new ArrayList<>();
        example.define(gs);
        example.analyze(gs, classes);
        for (ClassWriter class_ : classes)
        {
            byte[] bytes = class_.toByteArray();
            Path path = Paths.get("example.class");
            Files.write(
                path, bytes, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
        }
    }
}
