package uwu.smsgamer.serverscripter.commands;

public abstract class CommandManager {
    // TODO: Implement.
    //  Also make a prefix too (such as  b  for BungeeCord proxies or  v  for Velocity proxies)
    public void registerCommands() {
        // registerCommand(new ScriptCommand());
        // registerCommand(new ShellCommand());
    }

    public abstract void registerCommand(SCommand command);
}
