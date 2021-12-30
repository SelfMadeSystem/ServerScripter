package uwu.smsgamer.serverscripter.commands.commands;

import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.commands.CommandManager;
import uwu.smsgamer.serverscripter.commands.SCommand;
import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;
import uwu.smsgamer.serverscripter.senapi.utils.ChatUtils;
import uwu.smsgamer.serverscripter.shell.Shell;
import uwu.smsgamer.serverscripter.shell.ShellManager;

import java.util.*;
import java.util.stream.Collectors;

public class ShellCommand extends SCommand {
    public final ColouredStringVal removedShell;
    public final ColouredStringVal openedShell;
    public final ColouredStringVal shellNotFound;
    public ShellCommand(CommandManager manager) {
        super(manager, "Shell", "shell", "/%alias% <none, shell name>", "sh");
        removedShell = new ColouredStringVal(name + ".RemovedShell", config,"&cRemoved shell.");
        openedShell = new ColouredStringVal(name + ".Opened", config,"&aOpened shell %shell%.");
        shellNotFound = new ColouredStringVal(name + ".ShellNotFound", config,"&cShell %shell% not found.");
    }

    @Override
    public void execute(APlayerOfSomeSort sender, String alias, String[] args) {
        if (!testPermission(sender)) {
            return;
        }
        if (args.length < 1) {
            List<String> names = new ArrayList<>(Arrays.asList(ScripterLoader.getInstance().getShellNames()));
            names.add("None");
            sender.sendMessage(String.join(", ", names));
            return;
        }
        String shellName = args[0].toLowerCase();
        UUID uuid = sender.getUniqueId();
        if (shellName.equals("none")) {
            ShellManager.removeShell(uuid);
            sender.sendMessage(removedShell.getValue());
        } else if (ScripterLoader.getInstance().getShells().containsKey(shellName)) {
            Shell<?> shell = ScripterLoader.getInstance().getShells().get(shellName);
            shell.setShell(uuid);
            ChatUtils.sendMessage(sender, openedShell,
                    "%shell%", shell.name);
        } else {
            ChatUtils.sendMessage(sender, shellNotFound,
                    "%shell%", shellName);
        }
    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        if (!testPermissionSilent(aPlayerOfSomeSort)) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            List<String> shells = Arrays.stream(ScripterLoader.getInstance().getShellNames()).map(String::toLowerCase).collect(Collectors.toList());
            shells.add("None");
            return shells;
        }
        return new ArrayList<>();
    }
}
