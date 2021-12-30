package uwu.smsgamer.serverscripter.scripts;

import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class that scripts must implement
 * if you are to implement a scripting language.
 */
public abstract class Script {
    @Getter
    protected final File scriptFile;
    @Getter
    protected String scriptName;
    @Getter
    protected String scriptDescription = "";
    @Getter
    protected String scriptVersion = "";
    @Getter
    protected String scriptAuthor = "";
    protected boolean loaded;

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
    }

    /**
     * Load the script. Do not execute any of the script code.
     */
    protected abstract void loadScript();

    /**
     * Initialize the script. Execute the script code (yes ik the naming is weird pls forgive me).
     */
    public abstract void init();

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
}
