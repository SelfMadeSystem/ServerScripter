package uwu.smsgamer.serverscripter.groovy;

import me.godead.lilliputian.*;
import uwu.smsgamer.serverscripter.ScriptAddon;
import uwu.smsgamer.serverscripter.groovy.scripts.GrScriptLoader;

public class GroovyScriptAddon extends ScriptAddon {
    public GroovyScriptAddon() {
        this.name = "Groovy";
        this.version = "0.1";
    }

    @Override
    public void loadDependencies(DependencyBuilder builder) {
//        builder.addDependency(new Dependency(Repository.MAVENCENTRAL,
//                "org.codehaus.groovy", "groovy-all", "3.0.8"));
    }

    @Override
    public void load() {
        System.out.println("[GrScripter] Loading");
        GrScriptLoader.getInstance().loadScripts();
        GrScriptLoader.getInstance().initScripts();
    }

    @Override
    public void enable() {
        System.out.println("[GrScripter] Enabling");
        GrScriptLoader.getInstance().enableScripts();
    }

    @Override
    public void disable() {
        System.out.println("[GrScripter] Disabling");
        GrScriptLoader.getInstance().disableScripts();
    }

    @Override
    public void reload() {
        System.out.println("[GrScripter] Reloading");
        GrScriptLoader.getInstance().reloadScripts();
    }
}
