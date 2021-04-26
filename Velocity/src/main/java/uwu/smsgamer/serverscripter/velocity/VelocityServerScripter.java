package uwu.smsgamer.serverscripter.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.*;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.godead.lilliputian.DependencyBuilder;
import org.slf4j.Logger;
import uwu.smsgamer.senapi.Loader;
import uwu.smsgamer.serverscripter.ScripterLoader;

import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Path;
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

