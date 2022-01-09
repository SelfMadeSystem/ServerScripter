package uwu.smsgamer.serverscripter.bungee.shell;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.serverscripter.bungee.BungeeServerScripter;
import uwu.smsgamer.serverscripter.shell.PlayerShell;
import uwu.smsgamer.serverscripter.shell.ShellManager;

import java.util.UUID;

public class ShellListener implements Listener {
    private static ShellListener INSTANCE;

    {
        INSTANCE = this;
    }

    public static ShellListener getInstance() {
        if (INSTANCE == null) new ShellListener();
        return INSTANCE;
    }

    private ShellListener() {
    }

    @EventHandler
    public void onChat(@NotNull ChatEvent e) {
        if (e.getMessage().startsWith("/")) { // We don't want to handle commands
            return;
        }
        UUID uuid;
        if (e.getSender() instanceof ProxiedPlayer) {
            uuid = ((ProxiedPlayer) e.getSender()).getUniqueId();
        } else {
            uuid = new UUID(0, 0);
        }
        PlayerShell shell = ShellManager.getShell(uuid);
        if (shell != null) {
            e.setCancelled(true);
            shell.onCommand(e.getMessage());
        }
    }
}
