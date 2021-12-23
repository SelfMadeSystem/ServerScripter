package uwu.smsgamer.serverscripter.javascript.shell;

import uwu.smsgamer.serverscripter.shell.Shell;

import java.util.UUID;

public class JSShell extends Shell<JSPlayerShell> {
    private static JSShell instance;

    public static JSShell getInstance() {
        if (instance == null) {
            instance = new JSShell();
        }
        return instance;
    }

    private JSShell() {
        super("JavaScript");
    }

    @Override
    public JSPlayerShell createShell(UUID uuid) {
        return new JSPlayerShell(uuid);
    }
}
