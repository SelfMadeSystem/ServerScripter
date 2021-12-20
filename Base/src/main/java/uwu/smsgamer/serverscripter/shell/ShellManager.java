package uwu.smsgamer.serverscripter.shell;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public final class ShellManager {
    private ShellManager() {
        throw new AssertionError("No uwu.smsgamer.serverscripter.shell.ShellManager instances for you!");
    }

    public static Map<UUID, PlayerShell> activeShells = new java.util.HashMap<>();
    public static BiConsumer<UUID, String> onResponse = (uuid, response) -> {
    };
    public static BiConsumer<UUID, Exception> onError = (uuid, response) -> {
    };

    public static PlayerShell getShell(UUID uuid) {
        return activeShells.get(uuid);
    }

    public static void setShell(UUID uuid, PlayerShell shell) {
        activeShells.put(uuid, shell);
    }

    public static void removeShell(UUID uuid) {
        activeShells.remove(uuid);
    }
}
