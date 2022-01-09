package uwu.smsgamer.serverscripter.velocity.utils;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.UUID;

public class APlayerOfSomeSortImpl implements APlayerOfSomeSort {
    private final CommandSource source;

    public APlayerOfSomeSortImpl(CommandSource source) {
        this.source = source;
    }

    @Override
    public void sendMessage(String s) {
        source.sendMessage(Component.text(s));
    }

    @Override
    public UUID getUniqueId() {
        if (source instanceof Player) {
            return ((Player) source).getUniqueId();
        } else {
            return new UUID(0, 0);
        }
    }

    @Override
    public String getName() {
        if (source instanceof Player) {
            return ((Player) source).getUsername();
        } else {
            return "Console";
        }
    }

    @Override
    public boolean hasPermission(String s) {
        return source.hasPermission(s);
    }
}
