package uwu.smsgamer.serverscripter.bungee;

import lombok.Getter;
import me.godead.lilliputian.DependencyBuilder;

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
    protected String name;
    protected String version;

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
