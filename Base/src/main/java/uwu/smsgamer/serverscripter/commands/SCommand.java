package uwu.smsgamer.serverscripter.commands;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;
import uwu.smsgamer.serverscripter.senapi.utils.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SCommand {
    public static final String config = "Commands";
    public final CommandManager manager;
    public final String name;
    public final String description;
    public final String basePermission;
    public final ColouredStringVal usage;
    public final ColouredStringVal noPermission;
    public final List<String> aliases;
    public final List<String> lowerAliases;

    public SCommand(CommandManager manager, final String name, final String description, final String usage, final String... aliases) {
        this.manager = manager;
        this.name = manager.getPrefix() + name;
        this.description = description;
        this.basePermission = "scripter.commands." + this.name.toLowerCase();
        this.usage = new ColouredStringVal(name + ".Usage", config, usage);
        this.noPermission = new ColouredStringVal(name + ".NoPermission", config,
                "&cYou do not have permission to use this command!");
        this.aliases = Arrays.stream(aliases).map(s -> manager.getPrefix() + s)
                .collect(java.util.stream.Collectors.toList());
        this.lowerAliases = this.aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public abstract void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args);

    public abstract List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args);

    public void sendUsage(APlayerOfSomeSort aPlayerOfSomeSort, String alias) {
        aPlayerOfSomeSort.sendMessage(this.usage.getValue().replace("%alias%", alias));
    }

    public boolean testPermission(APlayerOfSomeSort target, String... addons) {
        if (testPermissionSilent(target, addons)) {
            return true;
        }

        String permissionMessage = noPermission.getValue();

        if (permissionMessage == null) {
            target.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
        } else if (permissionMessage.length() != 0) {
            String permission = basePermission;
            if (permission != null) permission += (addons.length > 0 ? "." + String.join(".", addons) : "");
            else permission = "";

            for (String line : permissionMessage.replace("%permission%", permission).split("\n")) {
                target.sendMessage(line);
            }
        }

        return false;
    }

    public boolean testPermissionSilent(APlayerOfSomeSort target, String... addons) {
        String permission = basePermission;
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

    public static List<String> filterCompletions(List<String> completions, String last) {
        Collections.sort(completions);
        String lower = last.toLowerCase();
        List<String> newCompletions = completions.stream().filter(s -> s.toLowerCase().startsWith(lower)).collect(Collectors.toList());
        if (newCompletions.isEmpty()) {
            return completions;
        }
        return newCompletions;
    }
}
