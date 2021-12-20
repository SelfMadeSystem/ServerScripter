package uwu.smsgamer.serverscripter.packetevents;

import io.github.retrooper.packetevents.PacketEvents;
import me.godead.lilliputian.DependencyBuilder;
import uwu.smsgamer.serverscripter.ScriptAddon;
import uwu.smsgamer.serverscripter.spigot.SpigotServerScripter;

public class PEAddon extends ScriptAddon {
    public PEAddon() {
        super("PacketEvents", "0.2", null);
    }

    @Override
    public void loadDependencies(DependencyBuilder builder) {
        // PacketEvents should be already loaded in the server.
    }

    @Override
    public void load() {
        PacketEvents.create(SpigotServerScripter.getInstance());
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void enable() {
        PacketEvents.get().registerListener(new ScriptPacketListener());
        PacketEvents.get().init();
    }

    @Override
    public void disable() {
        PacketEvents.get().terminate();
    }

    @Override
    public void reload() {

    }
}
