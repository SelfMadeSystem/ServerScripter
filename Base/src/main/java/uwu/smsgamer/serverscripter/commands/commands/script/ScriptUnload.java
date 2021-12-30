package uwu.smsgamer.serverscripter.commands.commands.script;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.List;

public class ScriptUnload extends ScriptCmd {
    public final ColouredStringVal help;
    public ScriptUnload() {
        super("unload");
        help = new ColouredStringVal("Script.UnloadHelp", config,"&c/%alias% <lang> unload <script>\n" +
                "&7Unloads the specified script (extension unneeded).");
    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {

    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        return null;
    }
}
