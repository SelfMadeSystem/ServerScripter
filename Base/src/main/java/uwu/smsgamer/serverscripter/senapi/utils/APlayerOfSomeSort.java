package uwu.smsgamer.serverscripter.senapi.utils;

import java.util.UUID;

/**
 * A player of some sort.
 */
public interface APlayerOfSomeSort {
    void sendMessage(String s);
    UUID getUniqueId();
    String getName();
    boolean hasPermission(String s);
}
