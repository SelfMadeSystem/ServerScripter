package uwu.smsgamer.serverscripter.javascript;

import me.godead.lilliputian.*;
import uwu.smsgamer.serverscripter.ScriptAddon;
import uwu.smsgamer.serverscripter.javascript.scripts.JSScriptLoader;

public class JSAddon extends ScriptAddon {
    public JSAddon() {
        super("JavaScript", "0.2");
    }

    @Override
    public void loadDependencies(DependencyBuilder builder) {
        // group: 'org.graalvm.truffle', name: 'truffle-api', version: '1.0.0-rc8'
        builder.addDependency(new Dependency(Repository.MAVENCENTRAL,
                "org.graalvm.truffle",
                "truffle-api",
                "1.0.0-rc8"));
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
