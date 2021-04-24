package uwu.smsgamer.serverscripter;

import de.leonhard.storage.Json;
import lombok.Getter;
import me.godead.lilliputian.DependencyBuilder;

import java.io.File;

@Getter
public abstract class ScriptAddon {
    File file;
    Json json;
    protected String name;
    protected String version;

    public Json getJson() {
        return json;
    }

    public abstract void loadDependencies(DependencyBuilder builder);

    public abstract void load();

    public abstract void enable();

    public abstract void disable();

    public abstract void reload();
}
