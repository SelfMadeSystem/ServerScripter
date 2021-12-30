package uwu.smsgamer.serverscripter.spigot.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.UUID;

public class APlayerOfSomeSortImpl implements APlayerOfSomeSort {
    private final CommandSender player;
    private final UUID uuid;

    public APlayerOfSomeSortImpl(CommandSender player) {
        this.player = player;
        this.uuid = player instanceof Player ? ((Player) player).getUniqueId() : new UUID(0, 0);
    }

    @Override
    public void sendMessage(String s) {
        player.sendMessage(s);
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public boolean hasPermission(String s) {
        return player.hasPermission(s);
    }
}
