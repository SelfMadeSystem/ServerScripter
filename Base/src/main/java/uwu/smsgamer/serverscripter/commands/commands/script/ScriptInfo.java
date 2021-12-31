package uwu.smsgamer.serverscripter.commands.commands.script;

import uwu.smsgamer.serverscripter.scripts.Script;
import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;
import uwu.smsgamer.serverscripter.senapi.utils.ChatUtils;

import java.util.Collections;
import java.util.List;

public class ScriptInfo extends ScriptCmd {
    public final ColouredStringVal help;
    public final ColouredStringVal info;

    public ScriptInfo() {
        super("info");
        help = new ColouredStringVal("Script.InfoHelp", config,"&c/%alias% <lang> info <script>\n" +
                "&7Lists all the scripts in the the specified language.");
        info = new ColouredStringVal("Script.Info", config,
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
            ChatUtils.sendMessage(aPlayerOfSomeSort, scriptNotFound,
                    "%alias%", alias,
                    "%lang%", lang,
                    "%script%", scriptName);
            return;
        }
        ChatUtils.sendMessage(aPlayerOfSomeSort, info,
                "%fileName%", script.getScriptFile().getName(),
                "%lang%", getLangName(lang),
                "%name%", script.getScriptName(),
                "%author%", script.getScriptAuthor(),
                "%description%", script.getScriptDescription(),
                "%version%", script.getScriptVersion());
    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        if (args.length == 3) {
            return getScripts(args[0]);
        }
        return Collections.emptyList();
    }
}
