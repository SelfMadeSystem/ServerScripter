package uwu.smsgamer.serverscripter.shell;

import java.util.*;

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
    public final StringBuffer buffer = new StringBuffer();
    public final Shell<?> shell;

    /**
     * Creates a new player shell.
     * @param uuid The UUID of the player.
     * @param shell The shell associated with the player.
     */
    protected PlayerShell(UUID uuid, Shell<?> shell) {
        this.uuid = uuid;
        this.shell = shell;
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
            if (tasks.size() > 0) {
                ShellManager.onAnnounce.accept(uuid, "Please wait for the previous command" + (tasks.size() > 1 ? "s" : "") + " to finish.");
                return;
            }
            command = command.replace("\\ ", " ");
            if (command.equals("\\")) {
                ShellManager.onAnnounce.accept(uuid, "Finished command.");
                command = buffer.toString();
                buffer.setLength(0);
            } else {
                ShellManager.onPrint.accept(uuid, "> " + command);
                if (buffer.length() > 0) {
                    buffer.append("\n").append(command);
                    ShellManager.onAnnounce.accept(uuid, "Unfinished command.");
                    return;
                }
            }
            TimerTask task;
            TimerTask checkTask;
            TimerTask[] checkTasks = new TimerTask[1]; // This is a hack to get around the fact that TimerTask.cancel() is not thread-safe.
            String finalCommand = command;
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        doExecute(finalCommand);
                    } catch (Throwable e) {
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
                    if (tasks.contains(task)) {
                        task.cancel();
                        tasks.remove(task);
                        ShellManager.onPrintError.accept(uuid, timeout());
                    }
                }
            };
            timer.schedule(checkTask, getTimeout());
        }
    }

    public void doExecute(String command) {
        Result result = execute(command);
        switch (result.response) {
            case UNFINISHED:
                buffer.append(result.output);
                ShellManager.onAnnounce.accept(uuid, "Unfinished command.");
                break;
            case EXIT:
                ShellManager.onAnnounce.accept(uuid, "Exit shell.");
                shell.removeShell(uuid);
        }
    }

    /**
     * Executes the command. This method is called in a separate thread.
     *
     * @param command The command to execute.
     * @return The response.
     */
    public abstract Result execute(String command);

    /**
     * Called when the command times out.
     *
     * @return The response.
     */
    public String timeout() {
        return "Command timed out.";
    }

    public static class Result {
        public enum Response {
            FINISHED,
            UNFINISHED,
            EXIT,
        }

        public static final Result UNFINISHED = new Result(Response.UNFINISHED);
        public static final Result EXIT = new Result(Response.EXIT);
        public static final Result EMPTY = new Result(Response.FINISHED, "");

        public final Response response;
        public final String output;

        public Result(Response response, String output) {
            this.response = response;
            this.output = output;
        }

        public Result(Response response) {
            this.response = response;
            this.output = null;
        }

        public Result(String output) {
            this(Response.FINISHED, output);
        }
    }
}
