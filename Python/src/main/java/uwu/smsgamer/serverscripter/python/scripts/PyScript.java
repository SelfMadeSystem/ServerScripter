package uwu.smsgamer.serverscripter.python.scripts;

import org.python.util.PythonInterpreter;
import uwu.smsgamer.serverscripter.scripts.Script;

import java.io.*;

public class PyScript extends Script {
    private PythonInterpreter interpreter;

    public PyScript(File scriptFile) {
        super(scriptFile);
        interpreter = new PythonInterpreter();
    }

    @Override
    protected void loadScript() {
        System.out.println("LoadScript: " + scriptFile.getName());
    }

    @Override
    public void init() {
        System.out.println("Init: " + scriptFile.getName());
        try {
            interpreter.execfile(new FileInputStream(getScriptFile()), getScriptFile().getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enable() {
        System.out.println("Enable: " + scriptFile.getName());
    }

    @Override
    public void disable() {
        System.out.println("Disable: " + scriptFile.getName());
    }

    @Override
    public void reload() {
        System.out.println("Reload: " + scriptFile.getName());
    }
}
