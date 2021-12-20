package uwu.smsgamer.serverscripter.spigot.command;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import uwu.smsgamer.senapi.utils.spigot.SConsolePlayer;
import uwu.smsgamer.serverscripter.*;
import uwu.smsgamer.serverscripter.shell.Shell;

import java.util.*;

public class CommandScript implements TabExecutor {
    private static CommandScript INSTANCE;
    private static String HELP_MESSAGE = "/script <addons:reload:shell> [shell name]";

    {
        INSTANCE = this;
    }

    public static CommandScript getInstance() {
        if (INSTANCE == null) new CommandScript();
        return INSTANCE;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!testPermission(command, sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(HELP_MESSAGE);
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "addons":
                    if (!testPermission(command, sender, "addons")) {
                        return true;
                    }
                    sender.sendMessage("Addons:");
                    Set<ScriptAddon> addons = ScripterLoader.getInstance().getAddons();
                    if (addons.size() == 0) {
                        sender.sendMessage("No addons.");
                        return true;
                    }
                    for (ScriptAddon addon : addons) {
                        sender.sendMessage(addon.getName() + " version " + addon.getVersion());
                    }
                    return true;
                case "reload":
                    if (!testPermission(command, sender, "reload")) {
                        return true;
                    }
                    ScripterLoader.getInstance().reloadAddons();
                    sender.sendMessage("Reloaded.");
                    return true;
                case "shell":
                    if (!testPermission(command, sender, "shell")) {
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(ScripterLoader.getInstance().getShellNames());
                        return true;
                    }
                    String shellName = args[1].toLowerCase();
                    if (ScripterLoader.getInstance().getShells().containsKey(shellName)) {
                        Shell<?> shell = ScripterLoader.getInstance().getShells().get(shellName);
                        shell.setShell(SConsolePlayer.getOfflinePlayer(sender).getUniqueId());
                        sender.sendMessage("Shell " + shellName + " opened.");
                    }
                    return true;
                default:
                    sender.sendMessage(HELP_MESSAGE);
                    return true;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public boolean testPermission(Command command, CommandSender target, String... addons) {
        if (testPermissionSilent(command, target, addons)) {
            return true;
        }

        String permissionMessage = command.getPermissionMessage();

        if (permissionMessage == null) {
            target.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
        } else if (permissionMessage.length() != 0) {
            String permission = command.getPermission();
            if (permission != null) permission += (addons.length > 0 ? "." + String.join(".", addons) : "");
            else permission = "";

            for (String line : permissionMessage.replace("<permission>", permission).split("\n")) {
                target.sendMessage(line);
            }
        }

        return false;
    }

    public boolean testPermissionSilent(Command command, CommandSender target, String... addons) {
        String permission = command.getPermission();
        if ((permission == null) || (permission.length() == 0)) {
            return true;
        }

        permission += (addons.length > 0 ? "." + String.join(".", addons) : "");

        for (String p : permission.split(";")) {
            if (target.hasPermission(p)) {
                return true;
            }
        }

        return false;
    }
}
