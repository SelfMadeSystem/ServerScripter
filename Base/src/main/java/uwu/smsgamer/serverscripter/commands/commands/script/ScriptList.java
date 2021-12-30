package uwu.smsgamer.serverscripter.commands.commands.script;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.Collections;
import java.util.List;

public class ScriptList extends ScriptCmd {
    public final ColouredStringVal help;
    public ScriptList() {
        super("list");
        help = new ColouredStringVal("Script.LoadHelp", config,"&c/%alias% <lang> list\n" +
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
