package uwu.smsgamer.serverscripter.groovy.scripts;

import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.scripts.ScriptsLoader;

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
        scriptFiles.removeIf(f -> !(f.getName().endsWith(".groovy") || f.getName().endsWith(".gvy") ||
                f.getName().endsWith(".gy") || f.getName().endsWith(".gsh")));
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
