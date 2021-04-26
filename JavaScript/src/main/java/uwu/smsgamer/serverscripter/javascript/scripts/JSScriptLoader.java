package uwu.smsgamer.serverscripter.javascript.scripts;

import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.scripts.ScriptsLoader;

import java.io.File;
import java.util.Set;

public class JSScriptLoader extends ScriptsLoader<JSScript> {
    private static JSScriptLoader INSTANCE;

    {
        INSTANCE = this;
    }

    public static JSScriptLoader getInstance() {
        if (INSTANCE == null) new JSScriptLoader();
        return INSTANCE;
    }

    @Override
    public Set<File> getScriptFiles() {
        Set<File> scriptFiles = super.getScriptFiles();
        scriptFiles.removeIf(f -> {
            String name = f.getName();
            return !(name.endsWith(".js") || name.endsWith(".cjs") || name.endsWith(".mjs") || name.endsWith(".es")
            || name.endsWith(".es6") || name.endsWith("jsx"));
        });
        return scriptFiles;
    }

    @Override
    public File getScriptDirectory() {
        return new File(ScripterLoader.getInstance().getScriptsDir(), "javascript");
    }

    @Override
    public JSScript newScript(File file) {
        return new JSScript(file);
    }
}
