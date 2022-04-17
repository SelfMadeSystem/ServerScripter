package uwu.smsgamer.serverscripter.javascript;

import uwu.smsgamer.serverscripter.lilliputian.Dependency;
import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;
import uwu.smsgamer.serverscripter.lilliputian.Repository;
import uwu.smsgamer.serverscripter.ScriptAddon;
import uwu.smsgamer.serverscripter.javascript.scripts.JSScriptLoader;
import uwu.smsgamer.serverscripter.javascript.shell.JSShell;

public class JSAddon extends ScriptAddon {
    private static JSAddon INSTANCE;

    {
        INSTANCE = this;
    }

    public static JSAddon getInstance() {
        if (INSTANCE == null) throw new IllegalStateException("Instance not initialized");
        return INSTANCE;
    }

    public JSAddon() {
        super("JavaScript", "0.4.0", JSShell.getInstance());
        INSTANCE = this;
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
        JSScriptLoader.getInstance().loadScripts();
        JSScriptLoader.getInstance().initScripts();
    }

    @Override
    public void enable() {
        JSScriptLoader.getInstance().enableScripts();
    }

    @Override
    public void disable() {
        JSScriptLoader.getInstance().disableScripts();
    }

    @Override
    public void reload() {
        JSScriptLoader.getInstance().reloadScripts();
    }
}
