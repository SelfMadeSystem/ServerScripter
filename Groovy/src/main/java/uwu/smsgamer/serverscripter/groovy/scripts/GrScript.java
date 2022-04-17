package uwu.smsgamer.serverscripter.groovy.scripts;

import groovy.lang.*;
import groovy.util.*;
import org.codehaus.groovy.control.*;
import uwu.smsgamer.serverscripter.groovy.GroovyScriptAddon;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

public class GrScript extends uwu.smsgamer.serverscripter.scripts.Script {
    private static final CompilerConfiguration config;
    private final GroovyScriptEngine engine;
    private final Binding binding;
    private groovy.lang.Script script;
    private Logger logger;

    static {
        config = new CompilerConfiguration();
        config.setTargetDirectory(GrScriptLoader.getInstance().getScriptDirectory());
    }

    public GrScript(File scriptFile) {
        super(scriptFile);
        logger = GroovyScriptAddon.getInstance().getLogger();
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
        logger.info("LoadScript: " + scriptFile.getName());
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
    protected void unloadScript() {
        logger.info("UnloadScript: " + scriptFile.getName());
        disable();
        script = null;
    }

    @Override
    public void init() {
        if (initialized) return;
        super.init();
        logger.info("Init: " + scriptFile.getName());
        script.run();
    }

    @Override
    public void enable() {
        logger.info("Enable: " + scriptFile.getName());
        try {
            script.invokeMethod("onEnable", null);
        } catch (MissingMethodException ignored) {
        }
    }

    @Override
    public void disable() {
        logger.info("Disable: " + scriptFile.getName());
        try {
            script.invokeMethod("onDisable", null);
        } catch (MissingMethodException ignored) {
        }
    }

    @Override
    public void reload() {
        logger.info("Reload: " + scriptFile.getName());
        try {
            script.invokeMethod("onReload", null);
        } catch (MissingMethodException ignored) {
        }
    }

    @Override
    public void setObject(String name, Object object) {
        binding.setVariable(name, object);
    }

    @Override
    public Object getObject(String name) {
        return binding.getVariable(name);
    }
}
