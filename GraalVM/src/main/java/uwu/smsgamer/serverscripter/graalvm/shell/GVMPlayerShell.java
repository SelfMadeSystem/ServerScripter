package uwu.smsgamer.serverscripter.graalvm.shell;

import org.graalvm.polyglot.Context;
import uwu.smsgamer.serverscripter.shell.PlayerShell;
import uwu.smsgamer.serverscripter.shell.Shell;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GVMPlayerShell extends PlayerShell {
    private String language;
    private Context context;
    private Map<String, Object> members = new HashMap<>();
    protected GVMPlayerShell(UUID uuid) {
        super(uuid, GVMShell.getInstance());
        context = Context.newBuilder().allowAllAccess(true).build();
    }

    @Override
    public Result execute(String command) {
        if (command == null || command.isEmpty()) {
            return Result.EMPTY;
        }
        if (command.startsWith("_gvm_setLanguage")) {
            if (setLanguage(command.split(" ")[1])) {
                return new Result("Language set to " + language);
            } else {
                return Result.EMPTY;
            }
        }
        return null;
    }

    private boolean setLanguage(String language) {
        this.language = language;
        return true;
    }

    @Override
    public void setObject(String name, Object object) {
        members.put(name, object);
    }
}
