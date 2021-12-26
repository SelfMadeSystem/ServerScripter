package uwu.smsgamer.serverscripter.groovy.shell;

import uwu.smsgamer.serverscripter.shell.Shell;

import java.util.UUID;

public class GrShell extends Shell<GrPlayerShell> {
    private static GrShell instance;

    public static GrShell getInstance() {
        if (instance == null) {
            instance = new GrShell();
        }
        return instance;
    }

    private GrShell() {
        super("Groovy");
    }

    @Override
    public GrPlayerShell createShell(UUID uuid) {
        return new GrPlayerShell(uuid);
    }
}
