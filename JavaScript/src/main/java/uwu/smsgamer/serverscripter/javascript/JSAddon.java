package uwu.smsgamer.serverscripter.javascript;

import uwu.smsgamer.serverscripter.lilliputian.Dependency;
import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;
import uwu.smsgamer.serverscripter.lilliputian.Repository;
import uwu.smsgamer.serverscripter.ScriptAddon;
import uwu.smsgamer.serverscripter.javascript.scripts.JSScriptLoader;
import uwu.smsgamer.serverscripter.javascript.shell.JSShell;

public class JSAddon extends ScriptAddon {
    public JSAddon() {
        super("JavaScript", "0.2", JSShell.getInstance());
    }

    @Override
    public void loadDependencies(DependencyBuilder builder) {
        builder.addDependency(new Dependency(Repository.MAVENCENTRAL,
                "org.mozilla",
                "rhino",
                "1.7.13"));
    }

    @Override
    public void load() {
        System.out.println("[JSScripter] Loading");
        JSScriptLoader.getInstance().loadScripts();
        JSScriptLoader.getInstance().initScripts();
    }

    @Override
    public void enable() {
        System.out.println("[JSScripter] Enabling");
        JSScriptLoader.getInstance().enableScripts();
    }

    @Override
    public void disable() {
        System.out.println("[JSScripter] Disabling");
        JSScriptLoader.getInstance().disableScripts();
    }

    @Override
    public void reload() {
        System.out.println("[JSScripter] Reloading");
        JSScriptLoader.getInstance().reloadScripts();
    }
}
