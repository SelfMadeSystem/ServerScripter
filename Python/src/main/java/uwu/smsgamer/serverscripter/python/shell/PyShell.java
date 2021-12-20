package uwu.smsgamer.serverscripter.python.shell;

import uwu.smsgamer.serverscripter.shell.Shell;

import java.util.UUID;

public class PyShell extends Shell<PyPlayerShell> {
    public PyShell() {
        super("Python");
    }

    @Override
    public PyPlayerShell createShell(UUID uuid) {
        return new PyPlayerShell(uuid);
    }
}
