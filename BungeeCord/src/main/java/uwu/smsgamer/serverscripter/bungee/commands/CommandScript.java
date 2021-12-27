package uwu.smsgamer.serverscripter.bungee.commands;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import uwu.smsgamer.serverscripter.*;

import java.util.*;

public class CommandScript extends Command {
    private static CommandScript INSTANCE;

    {
        INSTANCE = this;
    }

    public CommandScript() {
        super("bscript", "serverscripter.command.script.bungee");
    }

    public static CommandScript getInstance() {
        if (INSTANCE == null) new CommandScript();
        return INSTANCE;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            sender.sendMessage(TextComponent.fromLegacyText(this.getPermissionMessage()));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage("/bscript <addons:reload>");
        } else {
            if (args[0].equalsIgnoreCase("addons")) {
                sender.sendMessage("Addons:");
                Set<ScriptAddon> addons = ScripterLoader.getInstance().getAddons();
                if (addons.size() == 0) {
                    sender.sendMessage("No addons.");
                    return;
                }
                for (ScriptAddon addon : addons) {
                    sender.sendMessage(addon.getName() + " version " + addon.getVersion());
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                ScripterLoader.getInstance().reloadAddons();
                sender.sendMessage("Reloaded.");
            } else {
                sender.sendMessage("/bscript <addons:reload>");
            }
        }
    }
}
