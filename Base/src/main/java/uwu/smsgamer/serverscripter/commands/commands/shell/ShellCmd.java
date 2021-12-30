package uwu.smsgamer.serverscripter.commands.commands.shell;

import lombok.Getter;
import uwu.smsgamer.serverscripter.ScripterLoader;
import uwu.smsgamer.serverscripter.scripts.Script;
import uwu.smsgamer.serverscripter.scripts.ScriptsLoader;
import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

import java.util.List;

public abstract class ShellCmd {
    public final ColouredStringVal scriptNotFound;
    protected final String baseCommandName;
    @Getter
    protected final String name;

    public ShellCmd(String baseCommandName, String name) {
        this.baseCommandName = baseCommandName;
        this.name = name;
        this.scriptNotFound = new ColouredStringVal(baseCommandName + ".ScriptNotFound", "&cScript not found");
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
