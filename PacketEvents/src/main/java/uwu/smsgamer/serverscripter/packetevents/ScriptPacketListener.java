package uwu.smsgamer.serverscripter.packetevents;

import io.github.retrooper.packetevents.event.*;
import io.github.retrooper.packetevents.event.impl.*;

import java.util.*;
import java.util.function.Consumer;

public class ScriptPacketListener extends PacketListenerAbstract {
    private static ScriptPacketListener instance;
    // Not the cleanest way of doing it, but it works.
    public final Set<Consumer<PacketStatusReceiveEvent>> packetStatusReceiveFuns = new HashSet<>();
    public final Set<Consumer<PacketStatusSendEvent>> packetStatusSendFuns = new HashSet<>();
    public final Set<Consumer<PacketLoginReceiveEvent>> packetLoginReceiveFuns = new HashSet<>();
    public final Set<Consumer<PacketLoginSendEvent>> packetLoginSendFuns = new HashSet<>();
    public final Set<Consumer<PacketPlayReceiveEvent>> packetPlayReceiveFuns = new HashSet<>();
    public final Set<Consumer<PacketPlaySendEvent>> packetPlaySendFuns = new HashSet<>();
    public final Set<Consumer<PostPacketPlayReceiveEvent>> postPacketPlayReceiveFuns = new HashSet<>();
    public final Set<Consumer<PostPacketPlaySendEvent>> postPacketPlaySendFuns = new HashSet<>();
    public final Set<Consumer<PostPlayerInjectEvent>> postPlayerInjectFuns = new HashSet<>();
    public final Set<Consumer<PlayerInjectEvent>> playerInjectFuns = new HashSet<>();
    public final Set<Consumer<PlayerEjectEvent>> playerEjectFuns = new HashSet<>();
    public final Set<Consumer<PacketEvent>> packetEventExternalFuns = new HashSet<>();
    public final Set<Consumer<PacketHandshakeReceiveEvent>> packetHandshakeReceiveFuns = new HashSet<>();

    public static ScriptPacketListener getInstance() {
        if (instance == null) instance = new ScriptPacketListener();
        return instance;
    }

    @Override
    public void onPacketStatusReceive(PacketStatusReceiveEvent event) {
        callAll(packetStatusReceiveFuns, event);
    }

    @Override
    public void onPacketStatusSend(PacketStatusSendEvent event) {
        callAll(packetStatusSendFuns, event);
    }

    @Override
    public void onPacketLoginReceive(PacketLoginReceiveEvent event) {
        callAll(packetLoginReceiveFuns, event);
    }

    @Override
    public void onPacketLoginSend(PacketLoginSendEvent event) {
        callAll(packetLoginSendFuns, event);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        callAll(packetPlayReceiveFuns, event);
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        callAll(packetPlaySendFuns, event);
    }

    @Override
    public void onPostPacketPlayReceive(PostPacketPlayReceiveEvent event) {
        callAll(postPacketPlayReceiveFuns, event);
    }

    @Override
    public void onPostPacketPlaySend(PostPacketPlaySendEvent event) {
        callAll(postPacketPlaySendFuns, event);
    }

    @Override
    public void onPostPlayerInject(PostPlayerInjectEvent event) {
        callAll(postPlayerInjectFuns, event);
    }

    @Override
    public void onPlayerInject(PlayerInjectEvent event) {
        callAll(playerInjectFuns, event);
    }

    @Override
    public void onPlayerEject(PlayerEjectEvent event) {
        callAll(playerEjectFuns, event);
    }

    @Override
    public void onPacketEventExternal(PacketEvent event) {
        callAll(packetEventExternalFuns, event);
    }

    @Override
    public void onPacketHandshakeReceive(PacketHandshakeReceiveEvent event) {
        callAll(packetHandshakeReceiveFuns, event);
    }

    public <E extends PacketEvent>void callAll(Set<Consumer<E>> functions, E event) {
        for (Consumer<E> function : functions) {
            try {
                function.accept(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
