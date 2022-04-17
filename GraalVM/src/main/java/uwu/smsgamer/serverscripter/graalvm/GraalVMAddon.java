package uwu.smsgamer.serverscripter.graalvm;

import de.leonhard.storage.Config;
import org.graalvm.polyglot.Context;
import uwu.smsgamer.serverscripter.ScriptAddon;
import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.graalvm.scripts.GVMScriptLoader;
import uwu.smsgamer.serverscripter.graalvm.shell.GVMShell;
import uwu.smsgamer.serverscripter.lilliputian.DependencyBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GraalVMAddon extends ScriptAddon {
    public Config config;
    public List<String> availableLanguages;

    private static GraalVMAddon INSTANCE;

    {
        INSTANCE = this;
    }

    public static GraalVMAddon getInstance() {
        if (INSTANCE == null) throw new IllegalStateException("INSTANCE is null.");
        return INSTANCE;
    }

    public GraalVMAddon() {
        super("GraalVM", "0.4.0", GVMShell.getInstance());
        INSTANCE = this;
        config = new Config(new File(ScripterLoader.getInstance().getConfigDir(), "GraalVM-config.yml"));
        List<String> langs = new ArrayList<>(Context.create().getEngine().getLanguages().keySet());
        langs.remove("ruby"); // Ruby is not supported
        langs.remove("wasm"); // Wasm is not supported
        langs.remove("llvm"); // LLVM is not a language. User can add it manually if they want.
        config.setDefault("Languages", langs);
        availableLanguages = config.getStringList("Languages");
    }

    @Override
    public void loadDependencies(DependencyBuilder builder) {
        try {
            // Test for GraalVM
            // If it's not installed, it will throw an exception
            Class.forName("org.graalvm.home.Version");

            // Test to see if all languages are installed. If not, warn the user.
            Context ctx = Context.create();
            List<String> installedLanguages = new ArrayList<>(ctx.getEngine().getLanguages().keySet());
            installedLanguages.remove("ruby"); // Ruby is not supported
            installedLanguages.remove("wasm"); // Wasm is not supported
            List<String> missingLanguages = new ArrayList<>();
            for (String lang : availableLanguages) {
                if (!installedLanguages.contains(lang)) {
                    missingLanguages.add(lang);
                }
            }
            if (!missingLanguages.isEmpty()) {
                if (missingLanguages.contains("ruby")) {
                    logger.info("[GraalVMAddon] Ruby is not currently supported.");
                    missingLanguages.remove("ruby");
                }
                if (missingLanguages.contains("wasm")) {
                    logger.info("[GraalVMAddon] WebAssembly is not currently supported.");
                    missingLanguages.remove("wasm");
                }
                if (!missingLanguages.isEmpty()) {
                    logger.info("[GraalVMAddon] The following languages are missing: " + missingLanguages);
                    logger.info("[GraalVMAddon] Installed and supported languages: " + installedLanguages);
                }
            }
            availableLanguages.removeIf(lang -> !installedLanguages.contains(lang));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Not running on GraalVM. Make sure you have the latest version of GraalVM installed.");
        }
    }

    @Override
    public void load() {
        GVMScriptLoader.getInstance().loadScripts();
        GVMScriptLoader.getInstance().initScripts();
    }

    @Override
    public void enable() {
        GVMScriptLoader.getInstance().enableScripts();
    }

    @Override
    public void disable() {
        GVMScriptLoader.getInstance().disableScripts();
    }

    @Override
    public void reload() {
        GVMScriptLoader.getInstance().reloadScripts();
    }
}
