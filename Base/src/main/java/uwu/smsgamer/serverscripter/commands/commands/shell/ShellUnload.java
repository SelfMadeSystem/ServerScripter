package uwu.smsgamer.serverscripter.commands.commands.shell;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.List;

public class ShellUnload extends ShellCmd {
    public final ColouredStringVal help;
    public ShellUnload(String baseCommandName) {
        super(baseCommandName, "unload");
        help = new ColouredStringVal(baseCommandName + ".UnloadHelp", "&c/%alias% <lang> unload <script>\n" +
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
