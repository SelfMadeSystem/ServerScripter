package uwu.smsgamer.serverscripter.commands;

import uwu.smsgamer.serverscripter.senapi.config.ColouredStringVal;
import uwu.smsgamer.serverscripter.senapi.utils.APlayerOfSomeSort;

public abstract class SCommand {
    protected static final String config = "commands";
    public final String name;
    public final String description;
    public final ColouredStringVal usage;
    public final ColouredStringVal noPermission;
    public final String[] aliases;

    public SCommand(final String name, final String description, final String usage, final String[] aliases) {
        this.name = name;
        this.description = description;
        this.usage = new ColouredStringVal(name + ".Usage", config, usage);
        this.noPermission = new ColouredStringVal(name + ".NoPermission", config, "&cYou do not have permission to use this command!");
        this.aliases = aliases;
    }

    public abstract void execute(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args);
    public abstract String[] tabComplete(APlayerOfSomeSort aPlayerOfSomeSort, String alias, String[] args);
}
