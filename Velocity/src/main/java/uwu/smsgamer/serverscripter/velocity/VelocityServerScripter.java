package uwu.smsgamer.serverscripter.velocity;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.*;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.godead.lilliputian.DependencyBuilder;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;
import uwu.smsgamer.senapi.Loader;
import uwu.smsgamer.serverscripter.*;

import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Set;
import java.util.logging.LogManager;

@Plugin(
        id = "server_scripter",
        name = "ServerScripter",
        version = "0.1",
        description = "Scripting plugin for Spigot.",
        authors = {"Sms_Gamer_3808"}
)
@Getter
public class VelocityServerScripter implements Loader {
    @Getter
    private static VelocityServerScripter instance;
    private final ProxyServer server;
    private final Logger sl4jfLogger;
    private final Path dataPath;
    private final File dataFile;
    private final java.util.logging.Logger javaLogger;
    private final ScripterLoader scripterLoader;

    {
        instance = this;
    }

    @Inject
    public VelocityServerScripter(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.sl4jfLogger = logger;
        this.dataPath = dataDirectory;
        this.dataFile = dataDirectory.toFile();

        scripterLoader = new ScripterLoader((URLClassLoader) this.getClass().getClassLoader(), this);
        DependencyBuilder builder = scripterLoader.startDependencyBuilder();
        scripterLoader.loadAddons(builder);
        builder.loadDependencies();
        scripterLoader.loadAddons();
        this.javaLogger = LogManager.getLogManager().getLogger(logger.getName());

        getServer().getCommandManager().register(new BrigadierCommand(LiteralArgumentBuilder.<CommandSource>literal("vscript")
                .requires(src -> src.hasPermission("serverscripter.command.script.velocity"))
                .then(LiteralArgumentBuilder.<CommandSource>literal("addons").executes(src -> {
                    CommandSource source = src.getSource();
                    source.sendMessage(Component.text("Addons:"));
                    Set<ScriptAddon> addons = ScripterLoader.getInstance().getAddons();
                    if (addons.size() == 0) {
                        source.sendMessage(Component.text("No addons."));
                        return 1;
                    }
                    for (ScriptAddon addon : addons) {
                        source.sendMessage(Component.text(addon.getName() + " version " + addon.getVersion()));
                    }
                    return 1;
                }))
                .then(LiteralArgumentBuilder.<CommandSource>literal("reload").executes(src -> {
                    CommandSource source = src.getSource();
                    ScripterLoader.getInstance().reloadAddons();
                    source.sendMessage(Component.text("Reloaded."));
                    return 1;
                })).executes(src -> {
                    src.getSource().sendMessage(Component.text("/vscript <addons:reload>"));
                    return 1;
                }).build()));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        scripterLoader.enableAddons();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        scripterLoader.disableAddons();
    }

    @Subscribe
    public void onProxyReload(ProxyReloadEvent event) {
        scripterLoader.reloadAddons();
    }

    @Override
    public File getDataFolder() {
        return this.dataFile;
    }

    @Override
    public File getFile() {
        throw new UnsupportedOperationException("Velocity doesn't seem to provide an API for this.");
    }

    @Override
    public java.util.logging.Logger getLogger() {
        return javaLogger; //???
    }
}

