package uwu.smsgamer.serverscripter.python.scripts;

import uwu.smsgamer.serverscripter.scripts.Script;

import java.io.File;

public class PyScript extends Script {
    public PyScript(File scriptFile) {
        super(scriptFile);
    }

    @Override
    protected void loadScript() {
        System.out.println("LoadScript: " + scriptFile.getName());
    }

    @Override
    public void init() {
        System.out.println("Init: " + scriptFile.getName());
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
