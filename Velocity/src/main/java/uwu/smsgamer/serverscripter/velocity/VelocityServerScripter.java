package uwu.smsgamer.serverscripter.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;
import org.slf4j.Logger;
import uwu.smsgamer.serverscripter.ScriptLoader;
import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.shell.ShellManager;
import uwu.smsgamer.serverscripter.velocity.commands.VelocityCommandManager;
import uwu.smsgamer.serverscripter.velocity.shell.ShellListener;

import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.LogManager;

@Plugin(
        id = "server_scripter",
        name = "ServerScripter",
        version = "0.3.0",
        description = "Scripting plugin for Velocity.",
        authors = {"Sms_Gamer_3808"}
)
@Getter
public class VelocityServerScripter implements ScriptLoader {
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

        scripterLoader = new ScripterLoader(this);
        DependencyBuilder builder = scripterLoader.startDependencyBuilder();
        scripterLoader.loadAddons(builder);
        builder.loadDependencies();
        scripterLoader.loadAddons();
        this.javaLogger = LogManager.getLogManager().getLogger(logger.getName());


        ShellManager.onPrint = (uuid, message) -> {
            Optional<Player> optional = getServer().getPlayer(uuid);
            if (optional.isPresent()) {
                Player player = optional.get();
                player.sendMessage(Component.text((message)));
            }
        };
        ShellManager.onPrintError = (uuid, message) -> {
            Optional<Player> optional = getServer().getPlayer(uuid);
            if (optional.isPresent()) {
                Player player = optional.get();
                player.sendMessage(Component.text(message).color(NamedTextColor.RED));
            }
        };
        ShellManager.onError = (uuid, error) -> {
//            error.printStackTrace();
            String message = error.getMessage();
            if (message == null) message = error.getClass().getSimpleName();
            ShellManager.onPrintError.accept(uuid, message);
        };
        ShellManager.onAnnounce = (uuid, message) -> {
            Optional<Player> optional = getServer().getPlayer(uuid);
            if (optional.isPresent()) {
                Player player = optional.get();
                player.showTitle(Title.title(
                        Component.empty(),
                        Component.text(message),
                        Title.Times.of(
                                Duration.ofMillis(10 * (1000 / 20)),
                                Duration.ofMillis(60 * (1000 / 20)),
                                Duration.ofMillis(10 * (1000 / 20)))));
            }
        };
        ShellManager.getObjects = (uuid) -> {
            Optional<Player> optional = getServer().getPlayer(uuid);
            if (optional.isPresent()) {
                Player player = optional.get();
                return Collections.singletonMap("player", player);
            }
            return Collections.emptyMap();
        };
        scripterLoader.setObject("plugin", this);

        VelocityCommandManager.getInstance();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        getServer().getEventManager().register(this, ShellListener.getInstance());
        VelocityCommandManager.getInstance().registerCommands();
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

