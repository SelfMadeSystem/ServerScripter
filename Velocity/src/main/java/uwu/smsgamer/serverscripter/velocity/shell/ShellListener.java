package uwu.smsgamer.serverscripter.velocity.shell;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import uwu.smsgamer.serverscripter.shell.PlayerShell;
import uwu.smsgamer.serverscripter.shell.ShellManager;

import java.util.UUID;

public class ShellListener {
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

    @Subscribe(order = PostOrder.FIRST)
    public void onChat(PlayerChatEvent e) {
        if (e.getMessage().startsWith("/")) { // We don't want to handle commands
            return;
        }
        UUID uuid = e.getPlayer().getUniqueId();
        PlayerShell shell = ShellManager.getShell(uuid);
        if (shell != null) {
            e.setResult(PlayerChatEvent.ChatResult.denied());
            shell.onCommand(e.getMessage());
        }
    }
}
