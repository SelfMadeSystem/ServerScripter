package uwu.smsgamer.serverscripter;

import lombok.Getter;
import me.godead.lilliputian.DependencyBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import uwu.smsgamer.senapi.Loader;
import uwu.smsgamer.serverscripter.utils.ScriptListenerHelper;

import java.io.File;
import java.net.URLClassLoader;

@Plugin(name = "ServerScripter", version = "0.1")
@Description("Scripting plugin for Spigot.")
@Author("Sms_Gamer_3808")
@SoftDependency("PacketEvents")
@SoftDependency("PlaceholderAPI")
public class SpigotServerScripter extends JavaPlugin implements Loader {
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

    @Override
    public void onLoad() {
        scripterLoader = new ScripterLoader((URLClassLoader) this.getClassLoader(), this);
        DependencyBuilder builder = scripterLoader.startDependencyBuilder();
        scripterLoader.loadAddons(builder);
        builder.loadDependencies();
        scripterLoader.loadAddons();
    }

    @Override
    public void onEnable() {
        ScriptListenerHelper.init();
        scripterLoader.enableAddons();
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
