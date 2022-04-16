package uwu.smsgamer.serverscripter.graalvm.scripts;

import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.graalvm.GraalVMAddon;
import uwu.smsgamer.serverscripter.scripts.ScriptsLoader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class GVMScriptLoader extends ScriptsLoader<GVMScript> {

    {
        INSTANCE = this;
    }

    public static GVMScriptLoader getInstance() {
        if (INSTANCE == null) new GVMScriptLoader();
        return INSTANCE;
    }

    protected GVMScriptLoader() {
        super("GraalVM", "graalvm", "gvm");
    }

    @Override
    public Set<File> getScriptFiles() {
        Set<File> files = new java.util.HashSet<>();
        GraalVMAddon.getInstance().config.getStringList("Languages").forEach(lang -> {
            File dir = new File(getScriptDirectory(), lang);
            files.addAll(_getScriptFiles(dir));
        });
        return files;
    }

    private Set<File> _getScriptFiles(File dir) {

        if (!dir.exists()) {
            dir.mkdirs();
            dir.mkdir();
        }
        File[] ts = dir.listFiles();
        if (ts == null) return Collections.emptySet();
        return Arrays.stream(ts).filter(f -> !f.getName().startsWith("_")).collect(Collectors.toSet());
    }

    @Override
    public File getScriptDirectory() {
        return new File(ScripterLoader.getInstance().getScriptsDir(), "graalvm");
    }

    @Override
    public GVMScript newScript(File file) throws IOException {
        String language = file.getParentFile().getName();
        return new GVMScript(file, language);
    }

    private static GVMScriptLoader INSTANCE;

}
