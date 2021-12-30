package uwu.smsgamer.serverscripter.commands.commands.script;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.List;

public class ScriptLoad extends ScriptCmd {
    public final ColouredStringVal help;
    public ScriptLoad() {
        super("load");
        help = new ColouredStringVal("Script.LoadHelp", config,"&c/%alias% <lang> load <script>\n" +
                "&7Loads the specified script (extension unneeded).");
    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {

    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        return null;
    }
}
