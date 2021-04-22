package uwu.smsgamer.serverscripter;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import uwu.smsgamer.senapi.Loader;

import java.io.File;

@Plugin(name="ServerScripter", version="0.1")
@Description("Scripting plugin for Spigot.")
@Author("Sms_Gamer_3808")
@SoftDependency("PacketEvents")
@SoftDependency("PlaceholderAPI")
public class SpigotServerScripter extends JavaPlugin implements Loader {
    @Override
    public File getFile() {
        return super.getFile();
    }
}
