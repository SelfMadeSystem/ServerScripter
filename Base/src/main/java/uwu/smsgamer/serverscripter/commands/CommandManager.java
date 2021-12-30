package uwu.smsgamer.serverscripter.commands;

import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.serverscripter.commands.commands.ShellCommand;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommandManager {
    protected List<SCommand> commands = new ArrayList<>();

    protected void registerCommand(SCommand command) {
        commands.add(command);
    }

    protected abstract @NotNull String getPrefix();

    public void registerCommands() {
        // registerCommand(new ScriptCommand(this));
         registerCommand(new ShellCommand(this));
    }

    public SCommand getCommand(String name) {
        for (SCommand command : commands) {
            if (name.equals(command.name) || command.aliases.contains(name)) {
                return command;
            }
        }
        return null;
    }

    public void executeCommand(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        SCommand command = getCommand(alias);
        if (command != null) {
            command.execute(aPlayerOfSomeSort, alias, args);
        }
    }

    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        SCommand command = getCommand(alias);
        if (command != null) {
            return command.getTabCompletions(aPlayerOfSomeSort, alias, args);
        }
        return Collections.emptyList();
    }
}
