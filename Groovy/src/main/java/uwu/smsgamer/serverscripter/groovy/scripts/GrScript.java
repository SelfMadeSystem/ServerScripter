package uwu.smsgamer.serverscripter.groovy.scripts;

import groovy.lang.*;
import groovy.util.*;
import org.codehaus.groovy.control.*;

import java.io.*;
import java.net.*;

public class GrScript extends uwu.smsgamer.serverscripter.scripts.Script {
    private static final CompilerConfiguration config;
    private final GroovyScriptEngine engine;
    private final Binding binding;
    private groovy.lang.Script script;

    static {
        config = new CompilerConfiguration();
        config.setTargetDirectory(GrScriptLoader.getInstance().getScriptDirectory());
    }

    public GrScript(File scriptFile) {
        super(scriptFile);
        try {
            engine = new GroovyScriptEngine(new URL[]{GrScriptLoader.getInstance().getScriptDirectory().toURI().toURL()});
            engine.setConfig(config);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        binding = new Binding();
    }

    @Override
    protected void loadScript() {
        System.out.println("LoadScript: " + scriptFile.getName());
        File scriptFile = getScriptFile();
        try {
            findScriptInfo();
            script = engine.createScript(scriptFile.toURI().toString(), binding);
        } catch (CompilationFailedException | ResourceException | ScriptException | IOException e) {
            e.printStackTrace();
        }
    }

    // Finds script name, description, version, and author
    private void findScriptInfo() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(scriptFile));
            boolean inLongComment = false;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("//")) {
                    String[] split = line.substring(2).trim().split(" ");
                    if (split.length > 1) {
                        String key = split[0].toLowerCase();
                        if (key.endsWith(":")) {
                            key = key.substring(0, key.length() - 1);
                        }
                        String value = line.substring(line.indexOf(split[0]) + split[0].length() + 1);
                        switch (key) {
                            case "name":
                                scriptName = value;
                                break;
                            case "description":
                                scriptDescription = value;
                                break;
                            case "version":
                                scriptVersion = value;
                                break;
                            case "author":
                                scriptAuthor = value;
                                break;
                        }
                    }
                } else if (line.startsWith("/*") || inLongComment) {
                    inLongComment = !line.endsWith("*/");
                    String[] split;
                    if (line.startsWith("/*")) {
                        split = line.substring(2).trim().split(" ");
                    } else if (line.startsWith("*")) {
                        split = line.substring(1).split(" ");
                    } else {
                        split = line.split(" ");
                    }
                    if (split.length > 1) {
                        String key = split[0].toLowerCase();
                        if (key.endsWith(":")) {
                            key = key.substring(0, key.length() - 1);
                        }
                        String value = line.substring(line.indexOf(split[0]) + split[0].length() + 1);
                        switch (key) {
                            case "name":
                                scriptName = value;
                                break;
                            case "description":
                                scriptDescription = value;
                                break;
                            case "version":
                                scriptVersion = value;
                                break;
                            case "author":
                                scriptAuthor = value;
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error finding script info: " + scriptFile.getName(), e);
        }
    }

    @Override
    public void init() {
        System.out.println("Init: " + scriptFile.getName());
        script.run();
    }

    @Override
    public void enable() {
        System.out.println("Enable: " + scriptFile.getName());
        try {
            script.invokeMethod("onEnable", null);
        } catch (MissingMethodException ignored) {
        }
    }

    @Override
    public void disable() {
        System.out.println("Disable: " + scriptFile.getName());
        try {
            script.invokeMethod("onDisable", null);
        } catch (MissingMethodException ignored) {
        }
    }

    @Override
    public void reload() {
        System.out.println("Reload: " + scriptFile.getName());
        try {
            script.invokeMethod("onReload", null);
        } catch (MissingMethodException ignored) {
        }
    }
}
