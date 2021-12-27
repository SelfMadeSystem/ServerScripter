package uwu.smsgamer.serverscripter.groovy.shell;

import groovy.lang.Binding;
import org.apache.groovy.groovysh.Groovysh;
import org.codehaus.groovy.tools.shell.IO;
import uwu.smsgamer.serverscripter.lilliputian.Lilliputian;
import uwu.smsgamer.serverscripter.shell.PlayerStream;
import uwu.smsgamer.serverscripter.shell.PlayerShell;
import uwu.smsgamer.serverscripter.shell.ShellManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.UUID;

public class GrPlayerShell extends PlayerShell {
    private final Binding binding;
    private final Groovysh groovysh;
    private final PrintStream out;
    private final PrintStream err;

    protected GrPlayerShell(UUID uuid) {
        super(uuid, GrShell.getInstance());
        binding = new Binding();
        out = new PrintStream(new PlayerStream(uuid, false) {
            @Override
            public void flush() {
                print(AnsiMCUtils.ansiToMC(builder.toString()));
                builder.setLength(0);
            }
        });
        err = new PrintStream(new PlayerStream(uuid, true) {
            @Override
            public void flush() {
                print(AnsiMCUtils.ansiToMC(builder.toString()));
                builder.setLength(0);
            }
        });
        this.groovysh = new Groovysh(Lilliputian.getClassLoader(), binding,
                new IO(new InputStream() {
                    @Override
                    public int read() throws IOException {
                        throw new IOException("Not implemented");
                    }
                }, out, err));
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
//        if (command.startsWith(":")) {
//            ShellManager.onAnnounce.accept(uuid, "Oh no you don't!");
//            return Result.EMPTY;
//        }
        Object result = groovysh.execute(command);
        return new Result(String.valueOf(result));
    }
}
