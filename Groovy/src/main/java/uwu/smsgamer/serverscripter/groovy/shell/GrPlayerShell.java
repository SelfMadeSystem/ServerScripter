package uwu.smsgamer.serverscripter.groovy.shell;

import org.apache.groovy.groovysh.Groovysh;
import org.codehaus.groovy.tools.shell.IO;
import uwu.smsgamer.serverscripter.groovy.GroovyScriptAddon;
import uwu.smsgamer.serverscripter.groovy.scripts.GrScriptLoader;
import uwu.smsgamer.serverscripter.shell.PlayerStream;
import uwu.smsgamer.serverscripter.shell.PlayerShell;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.UUID;

public class GrPlayerShell extends PlayerShell {
    private final Groovysh groovysh;
    private final PrintStream out;
    private final PrintStream err;
    protected GrPlayerShell(UUID uuid) {
        super(uuid, GrShell.getInstance());
        out = new PrintStream(new PlayerStream(uuid, false));
        err = new PrintStream(new PlayerStream(uuid, true));
        this.groovysh = new Groovysh(new IO(
                new InputStream() {
                    @Override
                    public int read() throws IOException {
                        throw new IOException("Not implemented");
                    }
                },
                out,
                err));
    }

    @Override
    public Result doExecute(String command) {
        return execute(command);
    }

    @Override
    public Result execute(String command) {
        if (command == null || command.isEmpty()) {
            return Result.EMPTY;
        }
        Object result = groovysh.execute(command); // FIXME: 12/26/21 NoClassDefFoundError: groovy.lang.Script
        return new Result(String.valueOf(result));
    }
}
