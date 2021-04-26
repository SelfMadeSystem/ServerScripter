package uwu.smsgamer.serverscripter.python;

import me.godead.lilliputian.*;
import uwu.smsgamer.serverscripter.ScriptAddon;
import uwu.smsgamer.serverscripter.python.scripts.PyScriptLoader;

public class PythonScriptAddon extends ScriptAddon {
    public PythonScriptAddon() {
        this.name = "Python";
        this.version = "0.1";
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
    }

    @Override
    public void reload() {
        System.out.println("[PyScripter] Enabling");
        PyScriptLoader.getInstance().reloadScripts();
    }
}
