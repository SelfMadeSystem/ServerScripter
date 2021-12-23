package uwu.smsgamer.serverscripter.javascript.shell;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.ShellContextFactory;
import uwu.smsgamer.serverscripter.shell.PlayerShell;

import java.util.UUID;

public class JSPlayerShell extends PlayerShell {
    private final Context cx;
    private final Scriptable scope;
    protected JSPlayerShell(UUID uuid) {
        super(uuid, JSShell.getInstance());
        ShellContextFactory factory = new ShellContextFactory();
        factory.setLanguageVersion(Context.VERSION_ES6);
        cx = factory.enterContext();
        scope = cx.initStandardObjects();
    }

    @Override
    public Result execute(String command) {
        synchronized (cx) {
            Object result = cx.evaluateString(scope, command, "shell", 1, null);
            if (result == null) {
                return Result.UNFINISHED;
            }
            return new Result(Result.Response.FINISHED, result.toString());
        }
    }
}
