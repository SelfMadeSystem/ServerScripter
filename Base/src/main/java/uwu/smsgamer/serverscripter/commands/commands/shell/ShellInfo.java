package uwu.smsgamer.serverscripter.commands.commands.shell;

import uwu.smsgamer.serverscripter.scripts.Script;
import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;
import uwu.smsgamer.serverscripter.senapi.utils.ChatUtils;

import java.util.Collections;
import java.util.List;

public class ShellInfo extends ShellCmd {
    public final ColouredStringVal help;
    public final ColouredStringVal info;

    public ShellInfo(String baseCommandName) {
        super(baseCommandName, "info");
        help = new ColouredStringVal(baseCommandName + ".InfoHelp", "&c/%alias% <lang> info <script>\n" +
                "&7Lists all the scripts in the the specified language.");
        info = new ColouredStringVal(baseCommandName + ".Info",
                "&7File name: &c%fileName%\n" +
                        "&7Language: &c%lang%\n" +
                        "&7Script Name: &c%name%\n" +
                        "&7Author: &c%author%\n" +
                        "&7Description: &c%description%\n" +
                        "&7Version: &c%version%");

    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        if (args.length < 3) {
            ChatUtils.sendMessage(aPlayerOfSomeSort, help,
                    "%alias%", alias);
            return;
        }
        String lang = args[0];
        String scriptName = args[2];
        Script script = getScript(lang, scriptName);
        if (script == null) {
            ChatUtils.sendMessage(aPlayerOfSomeSort, scriptNotFound);
            return;
        }
        ChatUtils.sendMessage(aPlayerOfSomeSort, info,
                "%fileName%", script.getScriptFile().getName(),
                "%lang%", lang,
                "%name%", script.getScriptName(),
                "%author%", script.getScriptAuthor(),
                "%description%", script.getScriptDescription(),
                "%version%", script.getScriptVersion());
    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        return Collections.emptyList();
    }
}
