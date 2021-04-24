package uwu.smsgamer.serverscripter.scripts;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ScriptsLoader<S extends Script> {
    public final Set<S> scripts = new HashSet<>();

    public abstract File getScriptDirectory();

    public void loadScripts() {
        for (File scriptFile : getScriptFiles()) {
            S e = newScript(scriptFile);
            e.load();
            scripts.add(e);
        }
    }

    public abstract S newScript(File file);

    public Set<File> getScriptFiles() {
        File scriptDirectory = getScriptDirectory();
        if (!scriptDirectory.exists()) {
            scriptDirectory.mkdirs();
            scriptDirectory.mkdir();
        }
        File[] ts = scriptDirectory.listFiles();
        if (ts == null) return Collections.emptySet();
        return Arrays.stream(ts).collect(Collectors.toSet());
    }

    public void initScripts() {
        for (S script : scripts) {
            script.init();
        }
    }

    public void enableScripts() {
        for (S script : scripts) {
            script.enable();
        }
    }

    public void disableScripts() {
        for (S script : scripts) {
            script.disable();
        }
    }

    public void reloadScripts() {
        for (S script : scripts) {
            script.reload();
        }
    }
}
