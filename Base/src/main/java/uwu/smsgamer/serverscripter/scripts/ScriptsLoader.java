package uwu.smsgamer.serverscripter.scripts;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import uwu.smsgamer.serverscripter.ScripterLoader;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An abstract class that you must implement if
 * you are to implement a scripting language.
 * This class loads and generates scripts.
 * @param <S> The type of the script class used.
 */
@Getter
public abstract class ScriptsLoader<S extends Script> {
    protected final Set<S> scripts = new HashSet<>();
    protected final String name;
    protected final List<String> aliases;

    protected ScriptsLoader(String name, String... aliases) {
        this.name = name;
        this.aliases = Arrays.asList(aliases);
        ScripterLoader.getInstance().addScriptsLoader(this);
    }

    public S getScript(String name) {
        for (S script : scripts) {
            if (script.getScriptName().equalsIgnoreCase(name)) return script;
        }
        return null;
    }

    /**
     * Returns the directory where scripts are located.
     * <p>
     * This should generally return for example:
     * <pre>
     *     new File(ScripterLoader.getInstance().getScriptsDir(), "script-language")
     * </pre>
     * @return the directory where scripts are located.
     */
    public abstract File getScriptDirectory();

    /**
     * Loads script files.
     */
    public void loadScripts() {
        for (File scriptFile : getScriptFiles()) {
            S e = newScript(scriptFile);
            e.load();
            scripts.add(e);
            ScripterLoader.getInstance().getObjects().forEach(e::setObject);
        }
    }

    /**
     * Make a new script object based off of a file.
     * @param file The file to make the script object from.
     * @return a new script object.
     */
    public abstract S newScript(File file);

    /**
     * Returns a list of all the script files.
     * <p>
     * Files started with an underscore <code>_</code> should generally be excluded.
     * @return a list of all the script files.
     */
    public Set<File> getScriptFiles() {
        File scriptDirectory = getScriptDirectory();
        if (!scriptDirectory.exists()) {
            scriptDirectory.mkdirs();
            scriptDirectory.mkdir();
        }
        File[] ts = scriptDirectory.listFiles();
        if (ts == null) return Collections.emptySet();
        return Arrays.stream(ts).filter(f -> !f.getName().startsWith("_")).collect(Collectors.toSet());
    }

    /**
     * Gets a script file given a name. Null if not found.
     * @param fileName The name of the file.
     * @return the script file. Null if not found.
     */
    @Nullable
    public File getScriptFile(String fileName) {
        for (File file : getScriptFiles()) {
            String name = file.getName();
            name = name.substring(0, name.lastIndexOf('.'));
            if (name.equalsIgnoreCase(fileName)) return file;
        }
        return null;
    }

    /**
     * Calls the {@link Script#init()} method for all scripts.
     */
    public void initScripts() {
        for (S script : scripts) {
            try {
                script.init();
            } catch (Exception e) {
                new Exception("Failed to init script: " + script.getScriptFile().getName(), e).printStackTrace();
            }
        }
    }

    /**
     * Creates and loads a new script.
     * @param file The file to load the script from.
     * @return the new script.
     */
    public S createAndLoadScript(File file) {
        S script = newScript(file);
        script.load();
        scripts.add(script);
        return script;
    }

    /**
     * Calls the {@link Script#enable()} method for all scripts.
     */
    public void enableScripts() {
        for (S script : scripts) {
            try {
                script.enable();
            } catch (Exception e) {
                new Exception("Failed to enable script: " + script.getScriptFile().getName(), e).printStackTrace();
            }
        }
    }

    /**
     * Calls the {@link Script#disable()} method for all scripts.
     */
    public void disableScripts() {
        for (S script : scripts) {
            try {
                script.disable();
            } catch (Exception e) {
                new Exception("Failed to disable script: " + script.getScriptFile().getName(), e).printStackTrace();
            }
        }
    }

    /**
     * Calls the {@link Script#reload()} method for all scripts.
     */
    public void reloadScripts() {
        for (S script : scripts) {
            try {
                script.reload();
            } catch (Exception e) {
                new Exception("Failed to reload script: " + script.getScriptFile().getName(), e).printStackTrace();
            }
        }
    }
}
