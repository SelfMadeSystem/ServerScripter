package uwu.smsgamer.serverscripter.groovy;

import de.leonhard.storage.Config;
import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;
import uwu.smsgamer.serverscripter.*;
import uwu.smsgamer.serverscripter.groovy.scripts.GrScriptLoader;
import uwu.smsgamer.serverscripter.groovy.shell.GrShell;

import java.io.File;

public class GroovyScriptAddon extends ScriptAddon {
    public final Config config;
    public GroovyScriptAddon() {
        super("Groovy", "0.2", GrShell.getInstance());
        config = new Config(new File(ScripterLoader.getInstance().getConfigDir(), "Groovy-config.yml"));
        config.setDefault("Delete Class Cache", true);
    }

    private static GroovyScriptAddon INSTANCE;

    {
        INSTANCE = this;
    }

    public static GroovyScriptAddon getInstance() {
        if (INSTANCE == null) new GroovyScriptAddon();
        return INSTANCE;
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
        if (config.getBoolean("Delete Class Cache")) {
            File[] listFiles = GrScriptLoader.getInstance().getScriptDirectory()
                    .listFiles(pathname -> pathname.getName().endsWith(".class"));
            if (listFiles != null) for (File file : listFiles) file.delete();
        }
    }

    @Override
    public void reload() {
        System.out.println("[GrScripter] Reloading");
        GrScriptLoader.getInstance().reloadScripts();
    }
}
