package uwu.smsgamer.serverscripter.scripts;

import lombok.Getter;

import java.io.File;

/**
 * An abstract class that scripts must implement
 * if you are to implement a scripting language.
 */
@Getter
public abstract class Script {
    protected final File scriptFile;
    protected String scriptName;
    protected String scriptDescription = "";
    protected String scriptVersion = "";
    protected String scriptAuthor = "";
    protected boolean loaded;
    protected boolean initialized;

    /**
     * @param scriptFile The file this script represents.
     */
    public Script(File scriptFile) {
        this.scriptFile = scriptFile;
        int lastIndexOf = scriptFile.getName().lastIndexOf(".");
        scriptName = scriptFile.getName().substring(0, lastIndexOf);
    }

    /**
     * Call this to load the script.
     * <p>
     * This calls {@link Script#loadScript()} if it is not already loaded.
     */
    public final void load() {
        if (!loaded) loadScript();
        loaded = true;
        setObject("script", this);
    }

    /**
     * Load the script. Do not execute any of the script code.
     */
    protected abstract void loadScript();

    /**
     * Call this to unload the script.
     * <p>
     * This calls {@link Script#unloadScript()} if it is loaded.
     */
    public final void unload() {
        if (loaded) unloadScript();
        loaded = false;
    }

    /**
     * Unload the script. Should call {@link Script#disable()} first.
     */
    protected abstract void unloadScript();

    /**
     * Initialize the script. Execute the script code (yes ik the naming is weird pls forgive me).
     */
    public void init() {
        if (initialized) return;
        initialized = true;
    }

    /**
     * Call enable functions in the script.
     */
    public abstract void enable();

    /**
     * Call disable functions in the script.
     */
    public abstract void disable();

    /**
     * Call reload functions in the script.
     */
    public abstract void reload();

    /**
     * Sets an object to be used by the script.
     *
     * @param name   The name of the object.
     * @param object The object.
     */
    public abstract void setObject(String name, Object object);

    /**
     * Gets an object from the script.
     *
     * @param name The name of the object.
     * @return The object.
     */
    public abstract Object getObject(String name);
}
