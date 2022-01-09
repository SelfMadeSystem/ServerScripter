package uwu.smsgamer.serverscripter.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.serverscripter.bungee.BungeeServerScripter;
import uwu.smsgamer.serverscripter.bungee.utils.APlayerOfSomeSortImpl;
import uwu.smsgamer.serverscripter.commands.CommandManager;
import uwu.smsgamer.serverscripter.commands.SCommand;

public class BungeeCommandManager extends CommandManager {
    private static BungeeCommandManager INSTANCE;

    {
        INSTANCE = this;
    }

    public static BungeeCommandManager getInstance() {
        if (INSTANCE == null) new BungeeCommandManager();
        return INSTANCE;
    }

    private BungeeCommandManager() {
    }

    @Override
    protected @NotNull String getPrefix() {
        return "b";
    }

    @Override
    protected void registerCommand(SCommand command) {
        super.registerCommand(command);
        BungeeServerScripter.getInstance().getProxy().getPluginManager().registerCommand(BungeeServerScripter.getInstance(), new BungeeCommand(command));
    }

    private static final class BungeeCommand extends Command implements TabExecutor {
        private final SCommand command;
        public BungeeCommand(SCommand command) {
            super(command.name, command.basePermission, command.aliases.toArray(new String[0]));
            this.command = command;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            this.command.execute(new APlayerOfSomeSortImpl(sender), command.name, args);
        }

        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            return this.command.getTabCompletions(new APlayerOfSomeSortImpl(sender), command.name, args);
        }
    }
}
