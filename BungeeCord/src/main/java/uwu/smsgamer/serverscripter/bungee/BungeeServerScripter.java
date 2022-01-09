package uwu.smsgamer.serverscripter.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import uwu.smsgamer.serverscripter.bungee.commands.BungeeCommandManager;
import uwu.smsgamer.serverscripter.bungee.shell.ShellListener;
import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import uwu.smsgamer.serverscripter.ScriptLoader;
import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.bungee.commands.CommandScript;
import uwu.smsgamer.serverscripter.senapi.utils.ChatColor;
import uwu.smsgamer.serverscripter.shell.ShellManager;

import java.net.URLClassLoader;
import java.util.Collections;

public class BungeeServerScripter extends Plugin implements ScriptLoader {
    private static BungeeServerScripter INSTANCE;
    private ScripterLoader scripterLoader;

    {
        INSTANCE = this;
    }

    public static BungeeServerScripter getInstance() {
        if (INSTANCE == null) new BungeeServerScripter();
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        scripterLoader = new ScripterLoader(this);
        DependencyBuilder builder = scripterLoader.startDependencyBuilder();
        scripterLoader.loadAddons(builder);
        builder.loadDependencies();
        scripterLoader.loadAddons();

        ShellManager.onPrint = (uuid, message) -> getProxy().getPlayer(uuid).sendMessage(message);
        ShellManager.onPrintError = (uuid, message) -> getProxy().getPlayer(uuid).sendMessage(ChatColor.RED + message);
        ShellManager.onError = (uuid, error) -> {
//            error.printStackTrace();
            String message = error.getMessage();
            if (message == null) message = error.getClass().getSimpleName();
            ShellManager.onPrintError.accept(uuid, message);
        };
        ShellManager.onAnnounce = (uuid, message) -> getProxy().getPlayer(uuid).sendTitle(
                getProxy().createTitle().title(new TextComponent())
                        .subTitle(new TextComponent(message)).fadeIn(10).stay(60).fadeOut(10)
        );
        ShellManager.getObjects = (uuid) -> Collections.singletonMap("player", getProxy().getPlayer(uuid));
        scripterLoader.setObject("plugin", this);
    }

    @Override
    public void onEnable() {
        scripterLoader.enableAddons();
//        this.getProxy().getPluginManager().registerCommand(this, CommandScript.getInstance());
        this.getProxy().getPluginManager().registerListener(this, ShellListener.getInstance());
        BungeeCommandManager.getInstance().registerCommands();
    }

    @Override
    public void onDisable() {
        scripterLoader.disableAddons();
    }
}
