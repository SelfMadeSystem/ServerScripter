package uwu.smsgamer.serverscripter.utils;

public interface KillingTimerTask extends Runnable {
    default void killed(boolean suspended) {
    }
}
