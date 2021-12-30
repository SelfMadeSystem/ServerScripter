package uwu.smsgamer.serverscripter.commands.commands.shell;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.Collections;
import java.util.List;

public class ShellCall extends ShellCmd {
    public final ColouredStringVal help;
    public ShellCall(String baseCommandName) {
        super(baseCommandName, "call");
        help = new ColouredStringVal(baseCommandName + ".CallHelp", "&c/%alias% <lang> call <script> <function> [args...]\n" +
                "&7Calls the specified function in the script.");;
    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {

    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        return Collections.emptyList();
    }
}
