package uwu.smsgamer.serverscripter.python;

import de.leonhard.storage.Config;
import uwu.smsgamer.serverscripter.lilliputian.Dependency;
import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;
import uwu.smsgamer.serverscripter.lilliputian.Repository;
import uwu.smsgamer.serverscripter.*;
import uwu.smsgamer.serverscripter.python.scripts.PyScriptLoader;
import uwu.smsgamer.serverscripter.python.shell.PyShell;

import java.io.File;

public class PythonScriptAddon extends ScriptAddon {
    public final Config config;

    public PythonScriptAddon() {
        super("Python", "0.2", PyShell.getInstance());
        config = new Config(new File(ScripterLoader.getInstance().getConfigDir(), "Python-config.yml"));
        config.setDefault("Delete Class Cache", true);
    }

    @Override
    public void loadDependencies(DependencyBuilder builder) {
        builder.addDependency(new Dependency(Repository.MAVENCENTRAL,
                "org.python", "jython-standalone", "2.7.2"));
    }

    @Override
    public void load() {
        System.out.println("[PyScripter] Loading");
        PyScriptLoader.getInstance().loadScripts();
        PyScriptLoader.getInstance().initScripts();
    }

    @Override
    public void enable() {
        System.out.println("[PyScripter] Enabling");
        PyScriptLoader.getInstance().enableScripts();
    }

    @Override
    public void disable() {
        System.out.println("[PyScripter] Disabling");
        PyScriptLoader.getInstance().disableScripts();
        if (config.getBoolean("Delete Class Cache")) {
            File[] listFiles = PyScriptLoader.getInstance().getScriptDirectory()
                    .listFiles(pathname -> pathname.getName().endsWith("$py.class"));
            if (listFiles != null) for (File file : listFiles) file.delete();
        }
    }

    @Override
    public void reload() {
        System.out.println("[PyScripter] Reloading");
        config.forceReload();
        PyScriptLoader.getInstance().reloadScripts();
    }
}
