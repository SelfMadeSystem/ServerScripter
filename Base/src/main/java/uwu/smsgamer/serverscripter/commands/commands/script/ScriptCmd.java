package uwu.smsgamer.serverscripter.commands.commands.script;

import lombok.Getter;
import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.commands.SCommand;
import uwu.smsgamer.serverscripter.scripts.Script;
import uwu.smsgamer.serverscripter.scripts.ScriptsLoader;
import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.List;

public abstract class ScriptCmd {
    public static final String config = SCommand.config;
    public static final ColouredStringVal scriptNotFound;
    @Getter
    protected final String name;

    static {
        scriptNotFound = new ColouredStringVal("Shell.ScriptNotFound", config,"&cScript not found");
    }

    public ScriptCmd(String name) {
        this.name = name;
    }

    public abstract void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args);
    public abstract List<String> getTabCompletions(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args);

    public static ScriptsLoader<?> getScriptsLoader(String lang) {
        return ScripterLoader.getInstance().getScriptsLoadersByName().get(lang.toLowerCase());
    }

    public static Script getScript(String lang, String name) {
        ScriptsLoader<?> scriptsLoader = getScriptsLoader(lang);
        if (scriptsLoader == null) return null;
        return scriptsLoader.getScript(name);
    }
}
