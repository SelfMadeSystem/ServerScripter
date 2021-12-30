package uwu.smsgamer.serverscripter.commands.commands;

import uwu.smsgamer.serverscripter.commands.CommandManager;
import uwu.smsgamer.serverscripter.commands.SCommand;
import uwu.smsgamer.serverscripter.commands.commands.shell.*;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.HashMap;
import java.util.List;

public class ScriptCommand extends SCommand { // Todo: Implement permissions
    private HashMap<String, ShellCmd> commands = new HashMap<>();
    public ScriptCommand(CommandManager manager) {
        super(manager, "Script", "Manages scripts.",
                "&c/script <lang> <%commands%> [script]");
        putCommand(new ShellCall(this.name));
        putCommand(new ShellInfo(this.name));
        putCommand(new ShellList(this.name));
        putCommand(new ShellLoad(this.name));
        putCommand(new ShellUnload(this.name));
        putCommand(new ShellReload(this.name));
    }

    private void putCommand(ShellCmd cmd) {
        commands.put(cmd.getName(), cmd);
    }

    private List<String> getCommandNames() {
        List<String> names = new java.util.ArrayList<>();
        for (ShellCmd cmd : commands.values()) {
            names.add(cmd.getName());
        }
        return names;
    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        if (args.length == 0) {
            sendUsage(aPlayerOfSomeSort, alias);
            return;
        }
        ShellCmd cmd = commands.get(args[0]);
        if (cmd == null) {
            sendUsage(aPlayerOfSomeSort, alias);
            return;
        }
        cmd.execute(aPlayerOfSomeSort, alias, args);
    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        if (args.length == 0) {
            return getCommandNames();
        }
        ShellCmd cmd = commands.get(args[0]);
        if (cmd == null) {
            return getCommandNames();
        }
        return cmd.getTabCompletions(aPlayerOfSomeSort, alias, args);
    }
}
