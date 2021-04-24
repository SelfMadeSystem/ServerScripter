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

public final class ScripterLoader {
    private static ScripterLoader INSTANCE;
    @NotNull
    private final URLClassLoader classLoader;
    @Getter
    private final Loader loader;
    private final File addonsDir;
    @Getter
    private final File scriptsDir;
    private final Set<ScriptAddon> addons = new HashSet<>();

    {
        INSTANCE = this;
    }
    public ScripterLoader(@NotNull URLClassLoader classLoader, Loader loader) {
        this.classLoader = classLoader;
        this.loader = loader;
        this.addonsDir = new File(this.loader.getDataFolder(), "addons");
        this.scriptsDir = new File(this.loader.getDataFolder(), "scripts");
    }

    public static ScripterLoader getInstance() {
        return INSTANCE;
    }

    public DependencyBuilder startDependencyBuilder() {
        return new Lilliputian(loader).getDependencyBuilder()
                .addDependency(new Dependency(Repository.MAVENCENTRAL,
                        "org.xerial",
                        "sqlite-jdbc",
                        "3.8.11.2"));
    }

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

//                    String name = json.getString("name");
//                    String version = json.getString("version");
//                    String author = json.getString("author");
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

    public void loadAddons() {
        for (ScriptAddon addon : addons) {
            addon.load();
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

    public void enableAddons() {
        for (ScriptAddon addon : addons) {
            addon.enable();
        }
    }

    public void disableAddons() {
        for (ScriptAddon addon : addons) {
            addon.disable();
        }
    }

    public void reloadAddons() {
        for (ScriptAddon addon : addons) {
            addon.reload();
        }
    }
}
