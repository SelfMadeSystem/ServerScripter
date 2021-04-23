package uwu.smsgamer.serverscripter.scripts;

import lombok.Getter;

import java.io.File;

public abstract class Script {
    @Getter
    protected final File scriptFile;
    protected boolean loaded;

    public Script(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    public final void load() {
        if (!loaded) loadScript();
        loaded = true;
    }

    protected abstract void loadScript();

    public abstract void init();

    public abstract void enable();

    public abstract void disable();

    public abstract void reload();
}
