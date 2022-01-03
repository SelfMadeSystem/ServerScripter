package uwu.smsgamer.serverscripter.commands.commands.script;

import uwu.smsgamer.serverscripter.scripts.Script;
import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;
import uwu.smsgamer.serverscripter.senapi.utils.ChatUtils;

import java.util.List;

public class ScriptUnload extends ScriptCmd {
    public final ColouredStringVal help;
    public final ColouredStringVal scriptUnloaded;
    public final ColouredStringVal scriptAlreadyUnloaded;
    public ScriptUnload() {
        super("unload");
        help = new ColouredStringVal("Script.Unload.Help", config,"&c/%alias% <lang> unload <script>\n" +
                "&7Unloads the specified script (extension unneeded).");
        scriptUnloaded = new ColouredStringVal("Script.Unload.Unloaded", config,"&aScript unloaded.");
        scriptAlreadyUnloaded = new ColouredStringVal("Script.Unload.AlreadyUnloaded", config,
                "&cScript already unloaded.");
    }

    @Override
    public void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        if (args.length < 3) {
            ChatUtils.sendMessage(aPlayerOfSomeSort, help,
                    "%alias%", alias);
            return;
        }
        String lang = args[0];
        String langName = getLangName(lang);
        String scriptName = args[2];
        Script script = getScript(lang, scriptName);
        if (script == null) {
            ChatUtils.sendMessage(aPlayerOfSomeSort, scriptNotFound,
                    "%alias%", alias,
                    "%lang%", langName,
                    "%script%", scriptName);
            return;
        }
        if (script.isLoaded()) {
            script.unload();
            ChatUtils.sendMessage(aPlayerOfSomeSort, scriptUnloaded,
                    "%alias%", alias,
                    "%lang%", langName,
                    "%script%", scriptName);
        }
    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        return null;
    }
}
