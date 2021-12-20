package uwu.smsgamer.serverscripter.spigot.utils;

import org.bukkit.event.*;
import org.bukkit.plugin.*;
import uwu.smsgamer.serverscripter.spigot.SpigotServerScripter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * Helper class to make listening to bukkit events much easier. Not necessary though.
 */
public class ScriptListenerHelper extends RegisteredListener {
    public static ScriptListenerHelper lowestListener;
    public static ScriptListenerHelper lowListener;
    public static ScriptListenerHelper listener;
    public static ScriptListenerHelper highListener;
    public static ScriptListenerHelper highestListener;
    public static ScriptListenerHelper monitorListener;

    public static void init() {
        lowestListener = new ScriptListenerHelper(EventPriority.LOWEST, SpigotServerScripter.getInstance());
        lowListener = new ScriptListenerHelper(EventPriority.LOW, SpigotServerScripter.getInstance());
        listener = new ScriptListenerHelper(EventPriority.NORMAL, SpigotServerScripter.getInstance());
        highListener = new ScriptListenerHelper(EventPriority.HIGH, SpigotServerScripter.getInstance());
        highestListener = new ScriptListenerHelper(EventPriority.HIGHEST, SpigotServerScripter.getInstance());
        monitorListener = new ScriptListenerHelper(EventPriority.MONITOR, SpigotServerScripter.getInstance());
    }

    @SuppressWarnings("unused")
    public static void registerEvent(Class<? extends Event> type, EventPriority priority, Consumer<Event> function) {
        switch (priority) {
            case LOWEST:
                lowestListener.registerFunction(type, function);
                break;
            case LOW:
                lowListener.registerFunction(type, function);
                break;
            case NORMAL:
                listener.registerFunction(type, function);
                break;
            case HIGH:
                highListener.registerFunction(type, function);
                break;
            case HIGHEST:
                highestListener.registerFunction(type, function);
                break;
            case MONITOR:
                monitorListener.registerFunction(type, function);
                break;
        }
    }

    private static HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    private static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
              && !clazz.getSuperclass().equals(Event.class)
              && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }

    private final HashMap<Class<? extends Event>, Set<Consumer<Event>>> functions = new HashMap<>();

    public ScriptListenerHelper(EventPriority priority, Plugin plugin) {
        super(null, null, priority, plugin, false);
    }

    public void registerFunction(Class<? extends Event> event, Consumer<Event> fun) {
        functions.computeIfAbsent(event, k -> {
            getEventListeners(event).register(this);
            return new HashSet<>();
        }).add(fun);
    }

    @Override
    public Listener getListener() {
        return new Listener(){
            @Override
            public boolean equals(Object o) {
                return false;
            }
        };
    }

    @Override
    public void callEvent(Event event) {
        Set<Consumer<Event>> funs = functions.get(event.getClass());
        if (funs != null) {
            for (Consumer<Event> fun : funs) {
                fun.accept(event);
            }
        }
    }
}
