package uwu.smsgamer.serverscripter.utils;

import lombok.SneakyThrows;
import uwu.smsgamer.serverscripter.ScripterLoader;

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
    private final TimerTask killTask;
    private final KillingTimer timer;

    TimerTaskKill(KillingTimerTask task, Timer killTimer, long maxTime, KillingTimer timer) {
        this.task = task;
        this.killTimer = killTimer;
        this.timer = timer;
        TimerTaskKill self = this;
        killTask = new TimerTask() { // Assumes that the task is being run as soon as it is created.
            @Override
            public void run() {
                System.out.println("??? " + task);
                self.cancel();
            }
        };
        killTimer.schedule(killTask, maxTime);
    }

    public void run() {
        thread = Thread.currentThread();
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
    @SneakyThrows
    public void kill() {
        synchronized (task) {
            if (thread != null) {
                // This is the only way to kill a thread that has no sleep() or wait() within it.
                // Hopefully nothing's catching the exception.
                thread.stop();
                if (thread.isAlive()) { // If it's still alive, it's probably stuck in a loop.
                    ScripterLoader.getLogger().warning("Thread " + thread + " is still alive!");
                    killTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (thread != null && thread.isAlive()) { // If it's still alive, kill it.
                                ScripterLoader.getLogger().warning("Thread " + thread + " is still alive!");
                                thread.suspend(); // This is the last resort. Hopefully nothing bad happens.
                                task.killed(true);
                                ScripterLoader.getLogger().warning("Suspended thread " + thread + ".");
                                thread.resume();
                                // It absolutely breaks, but at this point, I don't care.
                                // If someone really wants to catch ThreadDeath, they can. It's their fault.
                                // Look, I tried.
                            } else {
                                task.killed(false);
                                ScripterLoader.getLogger().info("Stopped task " + task);
                                timer.makeTimer();
                            }
                        }
                    }, 250);
                } else {
                    task.killed(false);
                    ScripterLoader.getLogger().info("Stopped task " + task);
                    timer.makeTimer();
                }
            }
        }
    }
}
