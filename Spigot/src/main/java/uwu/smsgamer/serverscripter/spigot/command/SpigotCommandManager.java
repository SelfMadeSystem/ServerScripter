package uwu.smsgamer.serverscripter.spigot.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.serverscripter.commands.CommandManager;
import uwu.smsgamer.serverscripter.commands.SCommand;
import uwu.smsgamer.serverscripter.spigot.SpigotServerScripter;
import uwu.smsgamer.serverscripter.spigot.utils.APlayerOfSomeSortImpl;

import java.util.List;

public class SpigotCommandManager extends CommandManager implements TabExecutor {
    @Override
    protected void registerCommand(SCommand command) {
        super.registerCommand(command);
        PluginCommand cmd = SpigotServerScripter.getInstance().getCommand(command.name);
        if (cmd != null) {
            cmd.setExecutor(this);
            cmd.setTabCompleter(this);
        }
    }

    @Override
    protected @NotNull String getPrefix() {
        return "";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        executeCommand(new APlayerOfSomeSortImpl(sender), label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return getTabCompletions(new APlayerOfSomeSortImpl(sender), alias, args);
    }
}
