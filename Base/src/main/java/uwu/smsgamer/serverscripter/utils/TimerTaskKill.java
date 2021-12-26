package uwu.smsgamer.serverscripter.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Wrapper for {@link KillingTimerTask} that kills the task when it is
 * cancelled.
 */
class TimerTaskKill extends java.util.TimerTask {
    private Thread thread;
    private final KillingTimerTask task;
    private final Timer killTimer;
    private final long maxTime;

    TimerTaskKill(KillingTimerTask task, Timer killTimer, long maxTime) {
        this.task = task;
        this.killTimer = killTimer;
        this.maxTime = maxTime;
    }

    public void run() {
        thread = Thread.currentThread();
        TimerTask killTask = new TimerTask() {
            @Override
            public void run() {
                cancel();
            }
        };
        killTimer.schedule(killTask, maxTime);
        try {
            task.run();
        } finally {
            thread = null;
            killTask.cancel();
        }
    }

    public final boolean cancel() {
        boolean result = super.cancel();
        kill();
        return result;
    }

    // This method kills the thread.
    public void kill() {
        synchronized (task) {
            if (thread != null) {
                thread.interrupt();
                task.killed();
                System.out.println("Killed task " + task);
            }
        }
    }
}
