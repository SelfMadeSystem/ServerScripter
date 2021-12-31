package uwu.smsgamer.serverscripter.shell;

import uwu.smsgamer.serverscripter.ScripterLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Shell class.
 * <p>
 * A shell is a command line interface to execute commands.
 */
public abstract class Shell<S extends PlayerShell> {
    public final String name;
    public final Map<UUID, S> shells = new HashMap<>(0);

    protected Shell(String name) {
        this.name = name;
    }

    public void setShell(UUID uuid) {
        if (shells.containsKey(uuid)) {
            S shell = shells.get(uuid);
            ShellManager.setShell(uuid, shell);
            return;
        }
        S shell = createShell(uuid);
        shells.put(uuid, shell);
        ShellManager.setShell(uuid, shell);
        ScripterLoader.getInstance().getObjects().forEach(shell::setObject);
        ShellManager.getObjects.apply(uuid).forEach(shell::setObject);
    }

    public void removeShell(UUID uuid) {
        shells.remove(uuid);
        ShellManager.removeShell(uuid);
    }

    public S getShell(UUID uuid) {
        return shells.get(uuid);
    }

    public abstract S createShell(UUID uuid);

    public void run(UUID uuid, String command) {
        shells.get(uuid).onCommand(command);
    }
}
