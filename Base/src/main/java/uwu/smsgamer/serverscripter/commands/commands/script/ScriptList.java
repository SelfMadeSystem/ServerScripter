package uwu.smsgamer.serverscripter.commands.commands.script;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;
import uwu.smsgamer.serverscripter.senapi.utils.ChatUtils;

import java.util.Collections;
import java.util.List;

public class ScriptList extends ScriptCmd {
    public final ColouredStringVal help;
    public final ColouredStringVal list;
    public final ColouredStringVal join;
    public final ColouredStringVal noScripts;

    public ScriptList() {
        super("list");
        help = new ColouredStringVal("Script.List.Help", config, "&c/%alias% <lang> list\n" +
                "&7Lists all the scripts in the the specified language.");
        list = new ColouredStringVal("Script.List.List", config, "&7Scripts: &a%scripts%");
        join = new ColouredStringVal("Script.List.Join", config, "&7, &a");
        noScripts = new ColouredStringVal("Script.List.NoScriptsFound", config, "&cNo scripts found.");
    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        // /script <lang> list
        if (args.length != 2) {
            ChatUtils.sendMessage(aPlayerOfSomeSort, help,
                    "%alias%", alias);
        } else {
            String lang = args[0];
            List<String> scripts = getScripts(lang);
            if (scripts.isEmpty()) {
                ChatUtils.sendMessage(aPlayerOfSomeSort, noScripts,
                        "%alias%", alias,
                        "%lang%", lang);
                return;
            }
            ChatUtils.sendMessage(aPlayerOfSomeSort, list,
                    "%scripts%", String.join(join.getValue(), scripts),
                    "%alias%", alias);
        }
    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        return Collections.emptyList();
    }
}
