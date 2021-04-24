package uwu.smsgamer.serverscripter.python.scripts;

import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.scripts.ScriptsLoader;

import java.io.File;
import java.util.Set;

public class PyScriptLoader extends ScriptsLoader<PyScript> {
    private static PyScriptLoader INSTANCE;

    {
        INSTANCE = this;
    }

    public static PyScriptLoader getInstance() {
        if (INSTANCE == null) new PyScriptLoader();
        return INSTANCE;
    }

    @Override
    public Set<File> getScriptFiles() {
        Set<File> scriptFiles = super.getScriptFiles();
        scriptFiles.removeIf(f -> !f.getName().endsWith(".py"));
        return scriptFiles;
    }

    @Override
    public File getScriptDirectory() {
        return new File(ScripterLoader.getInstance().getScriptsDir(), "python");
    }

    @Override
    public PyScript newScript(File file) {
        return new PyScript(file);
    }
}
