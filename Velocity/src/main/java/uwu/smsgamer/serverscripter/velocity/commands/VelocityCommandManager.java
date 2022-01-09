package uwu.smsgamer.serverscripter.velocity.commands;

import com.velocitypowered.api.command.SimpleCommand;
import org.jetbrains.annotations.NotNull;
import uwu.smsgamer.serverscripter.commands.CommandManager;
import uwu.smsgamer.serverscripter.commands.SCommand;
import uwu.smsgamer.serverscripter.velocity.VelocityServerScripter;
import uwu.smsgamer.serverscripter.velocity.utils.APlayerOfSomeSortImpl;

import java.util.List;

public class VelocityCommandManager extends CommandManager {
    private static VelocityCommandManager INSTANCE;

    {
        INSTANCE = this;
    }

    public static VelocityCommandManager getInstance() {
        if (INSTANCE == null) new VelocityCommandManager();
        return INSTANCE;
    }

    private VelocityCommandManager() {
    }

    @Override
    protected @NotNull String getPrefix() {
        return "v";
    }

    @Override
    protected void registerCommand(SCommand command) {
        super.registerCommand(command);
        VelocityServerScripter.getInstance().getServer().getCommandManager().register(command.name, new BungeeCommand(command), command.aliases.toArray(new String[0]));
    }

    private static final class BungeeCommand implements SimpleCommand {
        private final SCommand command;
        public BungeeCommand(SCommand command) {
            this.command = command;
        }

        @Override
        public void execute(SimpleCommand.Invocation invocation) {
            this.command.execute(new APlayerOfSomeSortImpl(invocation.source()), command.name, invocation.arguments());
        }

        @Override
        public List<String> suggest(SimpleCommand.Invocation invocation) {
            return this.command.getTabCompletions(new APlayerOfSomeSortImpl(invocation.source()), command.name, invocation.arguments());
        }

        @Override
        public boolean hasPermission(SimpleCommand.Invocation invocation) {
            return this.command.testPermissionSilent(new APlayerOfSomeSortImpl(invocation.source()));
        }
    }
}
