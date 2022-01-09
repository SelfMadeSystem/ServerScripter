package uwu.smsgamer.serverscripter.bungee.utils;

import net.md_5.bungee.api.CommandSender;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.UUID;

public class APlayerOfSomeSortImpl implements APlayerOfSomeSort {
    private final CommandSender sender;

    public APlayerOfSomeSortImpl(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String s) {
        sender.sendMessage(s);
    }

    @Override
    public UUID getUniqueId() {
        if (sender instanceof net.md_5.bungee.api.connection.ProxiedPlayer) {
            return ((net.md_5.bungee.api.connection.ProxiedPlayer) sender).getUniqueId();
        } else {
            return new UUID(0, 0);
        }
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public boolean hasPermission(String s) {
        return sender.hasPermission(s);
    }
}
