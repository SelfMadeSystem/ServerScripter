package uwu.smsgamer.serverscripter.bungee;

import me.godead.lilliputian.DependencyBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import uwu.smsgamer.senapi.Loader;

import java.net.URLClassLoader;

public class BungeeServerScripter extends Plugin implements Loader {
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
    }

    @Override
    public void onDisable() {
        scripterLoader.disableAddons();
    }
}
