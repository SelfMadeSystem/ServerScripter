package uwu.smsgamer.serverscripter.commands.commands.script;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.List;

public class ScriptReload extends ScriptCmd {
    public final ColouredStringVal help;
    public ScriptReload() {
        super("reload");
        help = new ColouredStringVal("Script.ReloadHelp", config,"&c/%alias% <lang> reload <script>\n" +
                "&7Calls the reload function in the script. To fully reload the script, unload and load it.");
    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {

    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        return null;
    }
}
