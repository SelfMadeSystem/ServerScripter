package uwu.smsgamer.serverscripter;

import lombok.Getter;
import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;
import uwu.smsgamer.serverscripter.shell.Shell;

import java.io.File;
import java.util.Map;

/**
 * An abstract class that addons must extend.
 * <p>
 * Addons are typically used to implement scripting languages but may
 * also be used for anything else such as making use of an API easier.
 */
@Getter
public abstract class ScriptAddon {
    File file;
    Map<String, String> jsonMap;
    protected final String name;
    protected final String version;
    protected final Shell<?> shell;

    public ScriptAddon(String name, String version, Shell<?> shell) {
        this.name = name;
        this.version = version;
        this.shell = shell;
    }

    /**
     * Use this method to load dependencies before loading the addon.
     * @param builder The {@link DependencyBuilder}.
     */
    public abstract void loadDependencies(DependencyBuilder builder);

    /**
     * Called to load the addon.
     */
    public abstract void load();

    /**
     * Called to enable the addon.
     */
    public abstract void enable();

    /**
     * Called to disable the addon.
     */
    public abstract void disable();

    /**
     * Called to reload the addon.
     */
    public abstract void reload();
}
