package uwu.smsgamer.serverscripter.bungee;

import me.godead.lilliputian.DependencyBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import uwu.smsgamer.serverscripter.ScriptLoader;
import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.bungee.commands.CommandScript;
import uwu.smsgamer.serverscripter.shell.PlayerOut;

import java.net.URLClassLoader;
import java.util.UUID;

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
        scripterLoader = new ScripterLoader((URLClassLoader) this.getClass().getClassLoader(), this);
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
