package uwu.smsgamer.serverscripter.python;

import uwu.smsgamer.serverscripter.ScriptAddon;

public class PythonScriptAddon extends ScriptAddon {
    @Override
    public void load() {
        System.out.println("load");
    }

    @Override
    public void enable() {
        System.out.println("enable");
    }

    @Override
    public void disable() {
        System.out.println("disable");
    }

    @Override
    public void reload() {
        System.out.println("reload");
    }
}
