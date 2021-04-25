package uwu.smsgamer.serverscripter.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.event.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

public class ScriptListenerHelper {
    public static <E extends Event> void registerListener(Class<E> eventType, Consumer<E> consumer) {
        registerListener(eventType, consumer, EventPriority.NORMAL);
    }

    public static <E extends Event> void registerListener(Class<E> eventType, Consumer<E> consumer, byte priority) {
        try {
            Field eventBusField = PluginManager.class.getDeclaredField("eventBus");
            eventBusField.setAccessible(true);
            EventBus bus = (EventBus) eventBusField.get(ProxyServer.getInstance().getPluginManager());

            Field byListenerAndPriorityField = EventBus.class.getDeclaredField("byListenerAndPriority");
            Field lockField = EventBus.class.getDeclaredField("lock");
            byListenerAndPriorityField.setAccessible(true);
            lockField.setAccessible(true);

            Method bakeHandlers = EventBus.class.getDeclaredMethod("bakeHandlers", Class.class);
            bakeHandlers.setAccessible(true);

            Method acceptMethod = Consumer.class.getDeclaredMethod("accept", Object.class);

            Map<Class<?>, Map<Byte, Map<Object, Method[]>>> byListenerAndPriority =
                    (Map<Class<?>, Map<Byte, Map<Object, Method[]>>>) byListenerAndPriorityField.get(bus);
            Lock lock = (Lock) lockField.get(bus);

            lock.lock();

            try {
                Map<Byte, Map<Object, Method[]>> prioritiesMap = byListenerAndPriority.computeIfAbsent(eventType, k -> new HashMap<>());

                Map<Object, Method[]> currentPriorityMap = prioritiesMap.computeIfAbsent(priority, k -> new HashMap<>());
                currentPriorityMap.put(consumer, new Method[]{acceptMethod});

                bakeHandlers.invoke(bus, eventType);
            } finally {
                lock.unlock();
            }
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
