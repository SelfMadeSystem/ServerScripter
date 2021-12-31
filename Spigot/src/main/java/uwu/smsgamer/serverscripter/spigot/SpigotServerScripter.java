package uwu.smsgamer.serverscripter.spigot;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import uwu.smsgamer.serverscripter.ScriptLoader;
import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.commands.CommandManager;
import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;
import uwu.smsgamer.serverscripter.senapi.config.ConfigManager;
import uwu.smsgamer.serverscripter.shell.ShellManager;
import uwu.smsgamer.serverscripter.spigot.command.CommandScript;
import uwu.smsgamer.serverscripter.spigot.command.SpigotCommandManager;
import uwu.smsgamer.serverscripter.spigot.shell.ShellListener;
import uwu.smsgamer.serverscripter.spigot.utils.ScriptCommand;
import uwu.smsgamer.serverscripter.spigot.utils.ScriptListenerHelper;

import java.io.File;
import java.util.Collections;

@Plugin(name = "ServerScripter", version = "0.2.0")
@Description("Scripting plugin for Spigot.")
@Author("Sms_Gamer_3808")
@SoftDependency("PacketEvents")
@SoftDependency("PlaceholderAPI")
@Command(name = "script", desc = "Scripting plugin for Spigot.")
@Command(name = "shell", desc = "Shell plugin for Spigot.", aliases = {"sh"})
public class SpigotServerScripter extends JavaPlugin implements ScriptLoader {
    private static SpigotServerScripter INSTANCE;
    @Getter
    private ScripterLoader scripterLoader;

    {
        INSTANCE = this;
    }

    public static SpigotServerScripter getInstance() {
        if (INSTANCE == null) new SpigotServerScripter();
        return INSTANCE;
    }

    @Getter
    private final CommandManager commandManager = new SpigotCommandManager();

    @Override
    public void onLoad() {
        scripterLoader = new ScripterLoader(this);
        DependencyBuilder builder = scripterLoader.startDependencyBuilder();
        scripterLoader.loadAddons(builder);
        builder.loadDependencies();
        scripterLoader.loadAddons();

        ShellManager.onPrint = (uuid, message) -> Bukkit.getPlayer(uuid).sendMessage(message);
        ShellManager.onPrintError = (uuid, message) -> Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + message);
        ShellManager.onError = (uuid, error) -> {
            error.printStackTrace();
            String message = error.getMessage();
            if (message == null) message = error.getClass().getSimpleName();
            ShellManager.onPrintError.accept(uuid, message);
        };
        ShellManager.onAnnounce = (uuid, message) -> Bukkit.getPlayer(uuid).sendTitle("", message, 10, 60, 10);
        ShellManager.getObjects = (uuid) -> Collections.singletonMap("player", Bukkit.getPlayer(uuid));
        scripterLoader.setObject("plugin", this);
    }

    @Override
    public void onEnable() {
        ConfigManager.getInstance().setup("commands");
        ScriptListenerHelper.init();
        scripterLoader.enableAddons();
//        ScriptCommand command = new ScriptCommand("script", "ServerScripter command.", "/script <addons:reload>", Collections.emptyList(), CommandScript.getInstance(), CommandScript.getInstance());
//        command.setPermission("serverscripter.command.script.spigot");
        getServer().getPluginManager().registerEvents(new ShellListener(), this);
        commandManager.registerCommands();
        ConfigManager.getInstance().saveAll();
    }

    @Override
    public void onDisable() {
        scripterLoader.disableAddons();
    }

    @Override
    public File getFile() {
        return super.getFile();
    }
}
