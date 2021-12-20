package uwu.smsgamer.serverscripter.shell;

import java.util.*;
import java.util.function.Consumer;

/**
 * A shell for a player. This runs commands in a separate thread.
 * If a command takes longer than the specified timeout, it will
 * be terminated and the command will return an error.
 */
public abstract class PlayerShell {
    public boolean isEnabled = false;
    public final UUID uuid;
    public final List<TimerTask> tasks = new java.util.ArrayList<>();
    public final Timer timer;

    /**
     * Creates a new player shell.
     *  @param uuid The UUID of the player.
     */
    protected PlayerShell(UUID uuid) {
        this.uuid = uuid;
        this.timer = new Timer(uuid.toString(), true);
    }

    /**
     * Called when the shell is enabled.
     */
    public void onEnable() {
        isEnabled = true;
    }

    /**
     * Called when the shell is disabled.
     */
    public void onDisable() {
        isEnabled = false;
    }

    /**
     * Returns the timeout in milliseconds.
     *
     * @return The timeout in milliseconds.
     */
    public long getTimeout() {
        return 10000L;
    }

    /**
     * Called when a command is executed.
     *
     * If the command takes longer than the timeout, it will be terminated.
     *
     * @param command The command that was executed.
     */
    public void onCommand(String command) {
        synchronized (tasks) {
            TimerTask task;
            TimerTask checkTask;
            TimerTask[] checkTasks = new TimerTask[1]; // This is a hack to get around the fact that TimerTask.cancel() is not thread-safe.
            task = new TimerTask() {
                @Override
                public void run() {
                    ShellManager.onResponse.accept(uuid, "One");
                    try {
                        ShellManager.onResponse.accept(uuid, execute(command));
                    } catch (Exception e) {
                        ShellManager.onError.accept(uuid, e);
                    }
                    checkTasks[0].cancel();
                    tasks.remove(this);
                }
            };
            tasks.add(task);
            timer.schedule(task, 0L);
            checkTasks[0] = checkTask = new TimerTask() {
                @Override
                public void run() {
                    ShellManager.onResponse.accept(uuid, "Two A");
                    if (tasks.contains(task)) {
                        ShellManager.onResponse.accept(uuid, "Two B");
                        task.cancel();
                        tasks.remove(task);
                        ShellManager.onError.accept(uuid, timeout());
                    }
                }
            };
            timer.schedule(checkTask, getTimeout());
        }
    }

    /**
     * Executes the command. This method is called in a separate thread.
     *
     * @param command The command to execute.
     * @return The response.
     */
    public abstract String execute(String command);

    /**
     * Called when the command times out.
     *
     * @return The response.
     */
    public Exception timeout() {
        return new Exception("Command timed out.");
    }
}
