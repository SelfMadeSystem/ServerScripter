package uwu.smsgamer.serverscripter.utils;

import java.util.Timer;

/**
 * The same thing as {@link java.util.Timer}, but it kills the thread if a task is not completed in time.
 */
public class KillingTimer  {
    private long maxTime;
    private final String name;
    private final boolean isDaemon;
    private Timer timer;
    private final Timer killTimer;

    public KillingTimer(String name, boolean isDaemon, long maxTime) {
        this.name = name;
        this.isDaemon = isDaemon;
        this.maxTime = maxTime;
        makeTimer();
        this.killTimer = new Timer(name + "-Killer", isDaemon);
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public void schedule(KillingTimerTask task, long delay) {
        timer.schedule(new TimerTaskKill(task, killTimer, maxTime, this, timer), delay);
    }

    void makeTimer() {
        this.timer = new Timer(name, isDaemon);
    }
}