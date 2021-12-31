package uwu.smsgamer.serverscripter.commands.commands;

import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.commands.CommandManager;
import uwu.smsgamer.serverscripter.commands.SCommand;
import uwu.smsgamer.serverscripter.commands.commands.script.*;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ScriptCommand extends SCommand { // Todo: Implement permissions
    private final HashMap<String, ScriptCmd> commands = new HashMap<>();
    public ScriptCommand(CommandManager manager) {
        super(manager, "Script", "Manages scripts.",
                "&c/%alias% <lang> <%commands%> [script]");
        putCommand(new ScriptInfo());
        putCommand(new ScriptList());
        putCommand(new ScriptLoad());
        putCommand(new ScriptUnload());
        putCommand(new ScriptReload());
    }

    private void putCommand(ScriptCmd cmd) {
        commands.put(cmd.getName(), cmd);
    }

    private List<String> getCommandNames() {
        List<String> names = new java.util.ArrayList<>();
        for (ScriptCmd cmd : commands.values()) {
            names.add(cmd.getName());
        }
        return names;
    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        if (args.length < 2) {
            sendUsage(aPlayerOfSomeSort, alias);
            return;
        }
        ScriptCmd cmd = commands.get(args[1]);
        if (cmd == null) {
            sendUsage(aPlayerOfSomeSort, alias);
            return;
        }
        cmd.execute(aPlayerOfSomeSort, alias, args);
    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(ScripterLoader.getInstance().getScriptsLoadersByName().keySet());
        }
        if (args.length == 2) {
            return getCommandNames();
        }
        ScriptCmd cmd = commands.get(args[1]);
        if (cmd == null) {
            return Collections.emptyList();
        }
        return cmd.getTabCompletions(aPlayerOfSomeSort, alias, args);
    }

    @Override
    public void sendUsage(APlayerOfSomeSort aPlayerOfSomeSort, String alias) {
        aPlayerOfSomeSort.sendMessage(this.usage.getValue().replace("%alias%", alias)
                .replace("%commands%", String.join(", ", getCommandNames())));
    }
}
