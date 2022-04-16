package uwu.smsgamer.serverscripter.commands.commands.script;

import uwu.smsgamer.serverscripter.scripts.Script;
import uwu.smsgamer.serverscripter.scripts.ScriptsLoader;
import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;
import uwu.smsgamer.serverscripter.senapi.utils.ChatUtils;

import java.io.File;
import java.util.List;

public class ScriptLoad extends ScriptCmd {
    public final ColouredStringVal help;
    public final ColouredStringVal scriptAlreadyLoaded;
    public final ColouredStringVal scriptLoaded;
    public final ColouredStringVal scriptFailedToLoad;
    public ScriptLoad() {
        super("load");
        help = new ColouredStringVal("Script.Load.Help", config,"&c/%alias% <lang> load <script>\n" +
                "&7Loads the specified script (extension unneeded).");
        scriptAlreadyLoaded = new ColouredStringVal("Script.Load.AlreadyLoaded", config,
                "&cThe script &7%script% &cis already loaded in &7%lang%&c.");
        scriptLoaded = new ColouredStringVal("Script.Load.Loaded", config,
                "&aThe script &7%script% &ahas been loaded in &7%lang%&a.");
        scriptFailedToLoad = new ColouredStringVal("Script.Load.FailedToLoad", config,
                "&cThe script &7%script% &cfailed to load in &7%lang%&c.");
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
        String langName = getLangName(lang);
        try {
            Script script = getScript(lang, scriptName);
            if (script == null) {
                ScriptsLoader<?> loader = getScriptsLoader(lang);
                if (loader != null) {
                    File file = loader.getScriptFile(scriptName);
                    if (file != null) {
                        script = loader.createAndLoadScript(file);
                        script.init();
                        script.enable();
                        ChatUtils.sendMessage(aPlayerOfSomeSort, scriptLoaded,
                                "%alias%", alias,
                                "%script%", scriptName,
                                "%lang%", langName);
                        return;
                    }
                }
                ChatUtils.sendMessage(aPlayerOfSomeSort, scriptNotFound,
                        "%alias%", alias,
                        "%lang%", langName,
                        "%script%", scriptName);
                return;
            }

            if (script.isLoaded()) {
                ChatUtils.sendMessage(aPlayerOfSomeSort, scriptAlreadyLoaded,
                        "%alias%", alias,
                        "%lang%", langName,
                        "%script%", scriptName);
                return;
            }
            script.load();
            script.enable();
            ChatUtils.sendMessage(aPlayerOfSomeSort, scriptLoaded,
                    "%alias%", alias,
                    "%lang%", langName,
                    "%script%", scriptName);
        } catch (Exception e) {
            e.printStackTrace();
            ChatUtils.sendMessage(aPlayerOfSomeSort, scriptFailedToLoad,
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
