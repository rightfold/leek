package leek.c.diagnostics;

import java.nio.file.Path;

/**
 * Class for source locations.
 */
public final class SourceLocation
{
    public final Path path;
    public int line;
    public int column;

    public SourceLocation(Path path, int line, int column)
    {
        this.path = path;
        this.line = line;
        this.column = column;
    }
}
