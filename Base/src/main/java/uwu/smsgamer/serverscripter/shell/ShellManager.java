package uwu.smsgamer.serverscripter.shell;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ShellManager {
    private ShellManager() {
        throw new AssertionError("No uwu.smsgamer.serverscripter.shell.ShellManager instances for you!");
    }

    public static Map<UUID, PlayerShell> activeShells = new java.util.HashMap<>();
    public static BiConsumer<UUID, String> onPrint = (uuid, response) -> {
    };
    public static BiConsumer<UUID, String> onPrintError = (uuid, response) -> {
    };
    public static BiConsumer<UUID, Throwable> onError = (uuid, response) -> {
        String message = response.getMessage();
        if (message == null) message = response.getClass().getSimpleName();
        onPrintError.accept(uuid, message);
    };
    public static BiConsumer<UUID, String> onAnnounce = (uuid, response) -> {
    };
    public static Function<UUID, Map<String, Object>> getObjects = (uuid) -> new HashMap<>();

    public static PlayerShell getShell(UUID uuid) {
        return activeShells.get(uuid);
    }

    public static void setShell(UUID uuid, PlayerShell shell) {
        activeShells.put(uuid, shell);
        activeShells.get(uuid).onEnable();
    }

    public static void removeShell(UUID uuid) {
        if (!activeShells.containsKey(uuid)) {
            return;
        }
        activeShells.get(uuid).onDisable();
        activeShells.remove(uuid);
    }
}
