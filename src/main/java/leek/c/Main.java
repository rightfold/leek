package leek.c;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import leek.c.diagnostics.SourceLocation;

import leek.c.syntax.ArrayType;
import leek.c.syntax.BoolType;
import leek.c.syntax.Definition;
import leek.c.syntax.SubroutineDefinition;
import leek.c.syntax.ValueParameter;
import leek.c.syntax.VariableExpression;

import org.objectweb.asm.ClassWriter;

public final class Main
{
    private Main()
    {
    }

    public static void main(String[] args) throws Exception
    {
        SourceLocation sl = new SourceLocation(Paths.get("example.leek"), 1, 1);

        Definition example = new SubroutineDefinition(
            sl,
            /* name */ "example",
            SubroutineDefinition.SubroutineKind.Procedure,
            Arrays.asList(
                new ValueParameter(
                    sl, "variable1", new ArrayType(sl, new BoolType(sl))
                ),
                new ValueParameter(sl, "variable2", new BoolType(sl))
            ),
            /* returnType */ new BoolType(sl),
            /* body */ new VariableExpression(sl, "variable2")
        );

        List<ClassWriter> classes = new ArrayList<>();
        example.analyze(classes);
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
