package uwu.smsgamer.serverscripter.python.scripts;

import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.scripts.ScriptsLoader;

import java.io.File;

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
    public File getScriptDirectory() {
        return new File(ScripterLoader.getInstance().getScriptsDir(), "python");
    }

    @Override
    public PyScript newScript(File file) {
        return null;
    }
}
