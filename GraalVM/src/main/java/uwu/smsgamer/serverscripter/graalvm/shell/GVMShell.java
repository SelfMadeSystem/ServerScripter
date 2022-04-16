package uwu.smsgamer.serverscripter.graalvm.shell;

import uwu.smsgamer.serverscripter.shell.Shell;

import java.util.UUID;

public class GVMShell extends Shell<GVMPlayerShell> {
    private static GVMShell INSTANCE;

    {
        INSTANCE = this;
    }

    public static GVMShell getInstance() {
        if (INSTANCE == null) new GVMShell();
        return INSTANCE;
    }

    protected GVMShell() {
        super("GraalVM");
    }

    @Override
    public GVMPlayerShell createShell(UUID uuid) {
        return new GVMPlayerShell(uuid);
    }
}
