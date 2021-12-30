package uwu.smsgamer.serverscripter.commands.commands.shell;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.List;

public class ShellLoad extends ShellCmd{
    public final ColouredStringVal help;
    public ShellLoad(String baseCommandName) {
        super(baseCommandName, "load");
        help = new ColouredStringVal(baseCommandName + ".LoadHelp", "&c/%alias% <lang> load <script>\n" +
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
