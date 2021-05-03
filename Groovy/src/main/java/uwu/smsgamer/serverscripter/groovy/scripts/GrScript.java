package uwu.smsgamer.serverscripter.groovy.scripts;

import groovy.lang.*;
import groovy.util.*;
import org.codehaus.groovy.control.*;

import java.io.File;
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
    }

    @Override
    public void init() {
        System.out.println("Init: " + scriptFile.getName());
        try {
            script = engine.createScript(getScriptFile().toURI().toString(), binding);
        } catch (CompilationFailedException | ResourceException | ScriptException e) {
            e.printStackTrace();
        }
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
