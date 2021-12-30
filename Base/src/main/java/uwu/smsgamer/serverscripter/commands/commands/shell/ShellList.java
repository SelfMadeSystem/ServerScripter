package uwu.smsgamer.serverscripter.commands.commands.shell;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.Collections;
import java.util.List;

public class ShellList extends ShellCmd {
    public final ColouredStringVal help;
    public ShellList(String baseCommandName) {
        super(baseCommandName, "List");
        help = new ColouredStringVal(baseCommandName + ".LoadHelp", "&c/%alias% <lang> list\n" +
                "&7Lists all the scripts in the the specified language.");
    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {

    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        return Collections.emptyList();
    }
}
