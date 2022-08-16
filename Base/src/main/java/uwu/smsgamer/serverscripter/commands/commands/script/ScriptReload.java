package uwu.smsgamer.serverscripter.commands.commands.script;

import uwu.smsgamer.serverscripter.scripts.Script;
import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;
import uwu.smsgamer.serverscripter.senapi.utils.ChatUtils;

import java.util.Collections;
import java.util.List;

public class ScriptReload extends ScriptCmd {
    public final ColouredStringVal help;
    public final ColouredStringVal scriptReloaded;
    public final ColouredStringVal scriptNotLoaded;
    public ScriptReload() {
        super("reload");
        help = new ColouredStringVal("Script.Reload.Help", config,"&c/%alias% <lang> reload <script>\n" +
                "&7Calls the reload function in the script. To fully reload the script, unload and load it.");
        scriptReloaded = new ColouredStringVal("Script.Reload.Reloaded", config,"&aScript reloaded.");
        scriptNotLoaded = new ColouredStringVal("Script.Reload.NotLoaded", config,"&cScript not loaded.");
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
            script.reload();
            ChatUtils.sendMessage(aPlayerOfSomeSort, scriptReloaded,
                    "%alias%", alias,
                    "%lang%", langName,
                    "%script%", scriptName);
        } else {
            ChatUtils.sendMessage(aPlayerOfSomeSort, scriptNotLoaded,
                    "%alias%", alias,
                    "%lang%", langName,
                    "%script%", scriptName);
        }
    }

    @Override
    public List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args) {
        if (args.length == 3) {
            return getScripts(args[0]);
        }
        return Collections.emptyList();
    }
}
