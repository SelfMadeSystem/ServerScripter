package uwu.smsgamer.serverscripter.spigot.shell;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uwu.smsgamer.serverscripter.shell.PlayerShell;
import uwu.smsgamer.serverscripter.shell.ShellManager;

public class ShellListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        PlayerShell shell = ShellManager.getShell(e.getPlayer().getUniqueId());
        if (shell != null) {
            e.setCancelled(true);
            shell.onCommand(e.getMessage());
        }
    }
}
