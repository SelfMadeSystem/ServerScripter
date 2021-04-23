package uwu.smsgamer.serverscripter;

import de.leonhard.storage.Json;

import java.io.File;

public abstract class ScriptAddon {
    File file;
    Json json;

    public File getFile() {
        return file;
    }

    public Json getJson() {
        return json;
    }

    public abstract void load();

    public abstract void enable();

    public abstract void disable();

    public abstract void reload();
}
