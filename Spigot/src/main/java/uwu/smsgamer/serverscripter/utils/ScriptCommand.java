package uwu.smsgamer.serverscripter.utils;

import org.bukkit.*;
import org.bukkit.command.*;

import java.lang.reflect.*;
import java.util.*;

public class ScriptCommand extends Command {
    public CommandExecutor executor;
    public TabCompleter tabCompleter;

    public ScriptCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
        registerCommand(this);
    }

    public ScriptCommand(String name, String description, String usageMessage, List<String> aliases, CommandExecutor executor) {
        super(name, description, usageMessage, aliases);
        registerCommand(this);
        setExecutor(executor);
    }

    public ScriptCommand(String name, String description, String usageMessage, List<String> aliases, CommandExecutor executor, TabCompleter tabCompleter) {
        super(name, description, usageMessage, aliases);
        registerCommand(this);
        setExecutor(executor);
        setTabCompleter(tabCompleter);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (executor == null) {
            throw new IllegalStateException("Executor for PyCommand is null!");
        }
        return executor.onCommand(sender, this, label, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (tabCompleter == null) {
            return super.tabComplete(sender, alias, args);
        }
        // let bukkit handle exceptions
        List<String> result = tabCompleter.onTabComplete(sender, this, alias, args);
        return result == null ? super.tabComplete(sender, alias, args) : result;
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

    public void setTabCompleter(TabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    public static void registerCommand(Command command) {
        // Very Dirty Hack.
        try {
            getCommandMap(Bukkit.getServer()).register("spygotscripts", command);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
          IllegalArgumentException | InvocationTargetException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    private static SimpleCommandMap getCommandMap(Server server) throws NoSuchMethodException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
        Method getCommandMapMethod = server.getClass().getMethod("getCommandMap");
        return (SimpleCommandMap) getCommandMapMethod.invoke(server);
    }
}
