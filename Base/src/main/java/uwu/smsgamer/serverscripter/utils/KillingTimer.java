package uwu.smsgamer.serverscripter.utils;

import java.util.Timer;

/**
 * The same thing as {@link java.util.Timer}, but it kills the thread if a task is not completed in time.
 */
public class KillingTimer extends java.util.Timer {
    private long maxTime;
    private final Timer killTimer;

    public KillingTimer(long maxTime) {
        this.maxTime = maxTime;
        this.killTimer = new Timer("KillingTimer", true);
    }

    public KillingTimer() {
        this(10000);
    }

    public KillingTimer(String name, boolean isDaemon, long maxTime) {
        super(name, isDaemon);
        this.maxTime = maxTime;
        this.killTimer = new Timer(name + "-Killer", isDaemon);
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public void schedule(KillingTimerTask task, long delay) {
        super.schedule(new TimerTaskKill(task, killTimer, maxTime), delay);
    }
}