package uwu.smsgamer.serverscripter.groovy.scripts;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.codehaus.groovy.jsr223.*;
import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.scripts.Script;

import javax.script.ScriptException;
import java.io.*;
import java.net.*;
import java.util.*;

public class GrScript extends Script {
    protected static final GroovyScriptEngineFactory factory = new GroovyScriptEngineFactory();
    private GroovyScriptEngineImpl interpreter;
//    public List<PyFunction> enableFuns = new ArrayList<>();
//    public List<PyFunction> reloadFuns = new ArrayList<>();
//    public List<PyFunction> disableFuns = new ArrayList<>();

    public GrScript(File scriptFile) {
        super(scriptFile);
        interpreter = (GroovyScriptEngineImpl) factory.getScriptEngine();
    }

    @Override
    protected void loadScript() {
        System.out.println("LoadScript: " + scriptFile.getName());
    }

    @Override
    public void init() {
        System.out.println("Init: " + scriptFile.getName());
        try {
            interpreter.compile(new FileReader(getScriptFile())).eval();
        } catch (FileNotFoundException | ScriptException e) {
            e.printStackTrace();
        }
//        getGoodFuns();
    }

    @Override
    public void enable() {
        System.out.println("Enable: " + scriptFile.getName());
//        execAll(this.enableFuns);
    }

    @Override
    public void disable() {
        System.out.println("Disable: " + scriptFile.getName());
//        execAll(this.disableFuns);
    }

    @Override
    public void reload() {
        System.out.println("Reload: " + scriptFile.getName());
//        execAll(this.reloadFuns);
    }

//    public void getGoodFuns() {
//        getAndSet("onEnable", enableFuns);
//        getAndSet("on_enable", enableFuns);
//        getAndSet("enable", enableFuns);
//        getAndSet("onReload", reloadFuns);
//        getAndSet("on_reload", reloadFuns);
//        getAndSet("reload", reloadFuns);
//        getAndSet("onDisable", disableFuns);
//        getAndSet("on_disable", disableFuns);
//        getAndSet("disable", disableFuns);
//    }
//
//    private void getAndSet(String name, List<PyFunction> list) {
//        PyObject obj = this.interpreter.get(name);
//        if (obj instanceof PyFunction) list.add((PyFunction) obj);
//    }
//
//    public void execAll(List<PyFunction> funs) {
//        for (PyFunction fun : funs) {
//            fun.__call__();
//        }
//    }
}
