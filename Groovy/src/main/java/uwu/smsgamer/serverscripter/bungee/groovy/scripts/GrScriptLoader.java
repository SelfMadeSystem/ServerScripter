package uwu.smsgamer.serverscripter.bungee.groovy.scripts;

import uwu.smsgamer.serverscripter.bungee.ScripterLoader;
import uwu.smsgamer.serverscripter.bungee.scripts.ScriptsLoader;

import java.io.File;
import java.util.Set;

public class GrScriptLoader extends ScriptsLoader<GrScript> {
    private static GrScriptLoader INSTANCE;

    {
        INSTANCE = this;
    }

    public static GrScriptLoader getInstance() {
        if (INSTANCE == null) new GrScriptLoader();
        return INSTANCE;
    }

    @Override
    public Set<File> getScriptFiles() {
        Set<File> scriptFiles = super.getScriptFiles();
        scriptFiles.removeIf(f -> !f.getName().endsWith(".groovy"));
        return scriptFiles;
    }

    @Override
    public File getScriptDirectory() {
        return new File(ScripterLoader.getInstance().getScriptsDir(), "groovy");
    }

    @Override
    public GrScript newScript(File file) {
        return new GrScript(file);
    }
}
