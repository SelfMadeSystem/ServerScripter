package uwu.smsgamer.serverscripter.bungee;

import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import uwu.smsgamer.serverscripter.ScriptLoader;
import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.bungee.commands.CommandScript;

import java.net.URLClassLoader;

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
    }

    @Override
    public void onEnable() {
        scripterLoader.enableAddons();
        this.getProxy().getPluginManager().registerCommand(this, CommandScript.getInstance());
    }

    @Override
    public void onDisable() {
        scripterLoader.disableAddons();
    }
}
