package uwu.smsgamer.serverscripter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.leonhard.storage.Config;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.serverscripter.lilliputian.*;
import uwu.smsgamer.serverscripter.scripts.Script;
import uwu.smsgamer.serverscripter.scripts.ScriptsLoader;
import uwu.smsgamer.serverscripter.shell.Shell;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.function.Function;
import java.util.jar.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * The main addon loader class. Dunno why I called it "ScripterLoader".
 */
public final class ScripterLoader {
    private static ScripterLoader INSTANCE;
    @NotNull
    private final CustomClassLoader classLoader;
    @Getter
    private final ScriptLoader loader;
    private final File addonsDir;
    @Getter
    private final File scriptsDir;
    @Getter
    private final File configDir;
    @Getter
    private final Set<ScriptAddon> addons = new HashSet<>();
    @Getter
    private final Set<ScriptsLoader<?>> scriptsLoaders = new HashSet<>();
    @Getter
    private final Map<String, ScriptsLoader<?>> scriptsLoadersByName = new HashMap<>();
    @Getter
    private final Map<String, Object> objects = new HashMap<>();

    {
        INSTANCE = this;
    }

    /**
     * @param loader      The {@link ScriptLoader} to use.
     */
    public ScripterLoader(ScriptLoader loader) {
        this.loader = loader;
        Lilliputian.load(loader);
        this.classLoader = Lilliputian.getClassLoader();
        this.addonsDir = new File(this.loader.getDataFolder(), "addons");
        this.scriptsDir = new File(this.loader.getDataFolder(), "scripts");
        this.configDir = new File(this.loader.getDataFolder(), "configs");
    }

    public static ScripterLoader getInstance() {
        return INSTANCE;
    }

    public void setObject(String name, Object object) {
        objects.put(name, object);
    }

    public Object getObject(String name) {
        return objects.get(name);
    }

    /**
     * Used to start the dependency builder. Gets sqlite-jdbc by default.
     *
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
     *
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

                    if (entry == null) throw new Exception("scripter.json does not exist. Jar: " + jarFile.getName());

                    Gson json = new Gson();

                    Type type = new TypeToken<Map<String, String>>(){}.getType();

                    Map<String, String> map = json.fromJson(new InputStreamReader(jarFile.getInputStream(entry)), type);

                    String main = map.get("main");

                    if (main == null) throw new Exception("Main is null. Jar: " + jarFile.getName());
                    String mainClassEntry = main.replace(".", "/") + ".class";
                    JarEntry mainEntry = jarFile.getJarEntry(mainClassEntry);
                    if (mainEntry == null)
                        throw new Exception("Main entry is null. Main: " + mainClassEntry + " Jar: " + jarFile.getName());
                    loadDependency(file);
                    Class<?> mainClass = Class.forName(main, true, classLoader);

                    if (!ScriptAddon.class.isAssignableFrom(mainClass))
                        throw new Exception("Main class is not ScriptAddon. Main class: " + mainClass.getName());

                    ScriptAddon addon = (ScriptAddon) mainClass.newInstance();

                    if (addon.getName() == null || addon.getName().length() == 0)
                        throw new Exception("Name of addon not initialised. Jar: " + jarFile.getName());
                    if (addon.getVersion() == null || addon.getVersion().length() == 0)
                        throw new Exception("Version of addon not initialised. Jar: " + jarFile.getName());
                    if (addons.stream().anyMatch(a -> a.getName().equals(addon.getName())))
                        throw new Exception("Addon " + addon.getName() + " already initialized. Jar: " + jarFile.getName());

                    addon.file = file;
                    addon.jsonMap = map;

                    addon.loadDependencies(builder);

                    addons.add(addon);

                } catch (Exception | ExceptionInInitializerError e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadDependency(File file) {
        classLoader.loadJar(file);
    }

    private Map<String, Shell<?>> shells;
    private String[] shellNames;

    public Map<String, Shell<?>> getShells() {
        if (shells == null) {
            shells = this.addons.stream().map(ScriptAddon::getShell).filter(Objects::nonNull).collect(Collectors.toMap(s -> s.name.toLowerCase(), Function.identity()));
        }
        return shells;
    }

    public String[] getShellNames() {
        if (shellNames == null) {
            shellNames = this.addons.stream().map(ScriptAddon::getShell).filter(Objects::nonNull).map(s -> s.name).toArray(String[]::new);
        }
        return shellNames;
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
     * <p>
     * TODO: Load addons that aren't loaded.
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

    public <S extends Script> void addScriptsLoader(ScriptsLoader<?> scriptsLoader) {
        scriptsLoaders.add(scriptsLoader);
        scriptsLoadersByName.put(scriptsLoader.getName(), scriptsLoader);
    }
}