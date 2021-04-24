package uwu.smsgamer.serverscripter;

import de.leonhard.storage.Json;
import me.godead.lilliputian.*;
import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.senapi.Loader;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.ZipEntry;

public final class ScripterLoader {
    @NotNull
    private final URLClassLoader classLoader;
    private final Loader loader;
    private final File addonsDir;
    private final Set<ScriptAddon> addons = new HashSet<>();

    public ScripterLoader(@NotNull URLClassLoader classLoader, Loader loader) {
        this.classLoader = classLoader;
        this.loader = loader;
        this.addonsDir = new File(this.loader.getDataFolder(), "addons");
    }

    public DependencyBuilder startDependencyBuilder() {
        return new Lilliputian(loader).getDependencyBuilder()
                .addDependency(new Dependency(Repository.MAVENCENTRAL,
                        "org.xerial",
                        "sqlite-jdbc",
                        "3.8.11.2"))
                .addDependency(new Dependency(Repository.JITPACK,
                        "com.github.simplix-softworks",
                        "SimplixStorage",
                        "3.2.2"));
    }

    public void loadAddons() {
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

                    if (!ScriptAddon.class.isAssignableFrom(mainClass)) throw new Exception("Main class is not ScriptAddon. Main class: " + mainClass.getName());

                    ScriptAddon addon = (ScriptAddon) mainClass.newInstance();

                    addon.file = file;
                    addon.json = json;

                    addon.load();

                    addons.add(addon);

                } catch (Exception | ExceptionInInitializerError  e) {
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
