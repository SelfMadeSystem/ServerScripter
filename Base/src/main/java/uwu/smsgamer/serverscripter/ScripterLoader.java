package uwu.smsgamer.serverscripter;

import de.leonhard.storage.Json;
import lombok.Getter;
import me.godead.lilliputian.*;
import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.senapi.Loader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.ZipEntry;

/**
 * The main addon loader class. Dunno why I called it "ScripterLoader".
 */
public final class ScripterLoader {
    private static ScripterLoader INSTANCE;
    @NotNull
    private final URLClassLoader classLoader;
    @Getter
    private final Loader loader;
    private final File addonsDir;
    @Getter
    private final File scriptsDir;
    @Getter
    private final Set<ScriptAddon> addons = new HashSet<>();

    {
        INSTANCE = this;
    }

    /**
     * @param classLoader The {@link URLClassLoader} to use.
     * @param loader      The {@link Loader} to use.
     */
    public ScripterLoader(@NotNull URLClassLoader classLoader, Loader loader) {
        this.classLoader = classLoader;
        this.loader = loader;
        this.addonsDir = new File(this.loader.getDataFolder(), "addons");
        this.scriptsDir = new File(this.loader.getDataFolder(), "scripts");
    }

    public static ScripterLoader getInstance() {
        return INSTANCE;
    }

    /**
     * Used to start the dependency builder. Gets sqlite-jdbc by default.
     * @return Returns a {@link DependencyBuilder}.
     */
    public DependencyBuilder startDependencyBuilder() {
        return new Lilliputian(loader).getDependencyBuilder()
                .addDependency(new Dependency(Repository.MAVENCENTRAL,
                        "org.xerial",
                        "sqlite-jdbc",
                        "3.8.11.2"));
    }

    /**
     * Loads all the addons in the addons directory.
     * @param builder The {@link DependencyBuilder} returned by {@link ScripterLoader#startDependencyBuilder()}.
     */
    public void loadAddons(DependencyBuilder builder) {
        if (!addonsDir.exists()) {
            addonsDir.mkdirs();
            addonsDir.mkdir();
        }

        for (File file : addonsDir.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                try {
                    JarFile jarFile = new JarFile(file);
                    ZipEntry entry = jarFile.getEntry("scripter.json");

                    if (entry == null) throw new Exception("scripter.json is null.");

                    Json json = new Json("<jar>", null,
                            jarFile.getInputStream(entry));

                    String main = json.getString("main");

                    if (main == null) throw new Exception("Main is null.");
                    String mainClassEntry = main.replace(".", "/") + ".class";
                    JarEntry mainEntry = jarFile.getJarEntry(mainClassEntry);
                    if (mainEntry == null) throw new Exception("Main entry is null. Main: " + mainClassEntry);
                    loadDependency(file);
                    Class<?> mainClass = Class.forName(main, true, classLoader);

                    if (!ScriptAddon.class.isAssignableFrom(mainClass))
                        throw new Exception("Main class is not ScriptAddon. Main class: " + mainClass.getName());

                    ScriptAddon addon = (ScriptAddon) mainClass.newInstance();

                    if (addon.getName() == null || addon.getName().length() == 0)
                        throw new Exception("Name of addon not initialised.");
                    if (addon.getVersion() == null || addon.getVersion().length() == 0)
                        throw new Exception("Version of addon not initialised.");

                    addon.file = file;
                    addon.json = json;

                    addon.loadDependencies(builder);

                    addons.add(addon);

                } catch (Exception | ExceptionInInitializerError e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void loadDependency(File file) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, file.toURI().toURL());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Calls the {@link ScriptAddon#load()} method for all addons.
     */
    public void loadAddons() {
        for (ScriptAddon addon : addons) {
            try {
                addon.load();
            } catch (Exception e) {
                new Exception("Failed to load addon: " + addon.getName() + " version " + addon.getVersion(), e).printStackTrace();
            }
        }
    }

    /**
     * Calls the {@link ScriptAddon#enable()} method for all addons.
     */
    public void enableAddons() {
        for (ScriptAddon addon : addons) {
            try {
                addon.enable();
            } catch (Exception e) {
                new Exception("Failed to enable addon: " + addon.getName() + " version " + addon.getVersion(), e).printStackTrace();
            }
        }
    }

    /**
     * Calls the {@link ScriptAddon#disable()} method for all addons.
     */
    public void disableAddons() {
        for (ScriptAddon addon : addons) {
            try {
                addon.disable();
            } catch (Exception e) {
                new Exception("Failed to disable addon: " + addon.getName() + " version " + addon.getVersion(), e).printStackTrace();
            }
        }
    }

    /**
     * Calls the {@link ScriptAddon#reload()} method for all addons.
     */
    public void reloadAddons() {
        for (ScriptAddon addon : addons) {
            try {
                addon.reload();
            } catch (Exception e) {
                new Exception("Failed to reload addon: " + addon.getName() + " version " + addon.getVersion(), e).printStackTrace();
            }
        }
    }
}
