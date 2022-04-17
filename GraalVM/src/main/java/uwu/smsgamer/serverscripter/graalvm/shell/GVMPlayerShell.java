package uwu.smsgamer.serverscripter.graalvm.shell;

import org.graalvm.polyglot.*;
import uwu.smsgamer.serverscripter.shell.PlayerShell;
import uwu.smsgamer.serverscripter.shell.PlayerStream;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GVMPlayerShell extends PlayerShell {
    private String language;
    private final Context context;
    private final Map<String, Object> members = new HashMap<>();

    protected GVMPlayerShell(UUID uuid) {
        super(uuid, GVMShell.getInstance());
        context = Context.newBuilder()
                .allowAllAccess(true)
                .out(new PlayerStream(uuid, false))
                .err(new PlayerStream(uuid, true))
                .build();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (language == null) {
            print("Please set your language.");
            print("Available languages: \u00A7r" + context.getEngine().getLanguages().keySet());
        }
    }

    @Override
    public Result execute(String command) {
        if (command == null || command.isEmpty()) {
            return Result.EMPTY;
        }
        if (context.getEngine().getLanguages().containsKey(command)) {
            String s = setLanguage(command);
            if (s == null) {
                print("Language set to " + language);
                announce("Language set to " + language);
            } else {
                printError(s);
                announce("\u00A7cError setting language (look in chat)");
            }
            return Result.EMPTY;
        }
        if (language == null) {
            announce("Language not set");
            print("Available languages: \u00A7r" + context.getEngine().getLanguages().keySet());
            return Result.EMPTY;
        }
        Value result;
        try {
            Source source = Source.newBuilder(language, command, "<shell>")
                    .interactive(true).buildLiteral();
            result = context.eval(source);
        } catch (PolyglotException e) {
            if (e.isExit()) {
                return Result.EXIT;
            }
            throw e;
        }

        return new Result(result.toString());
    }

    private String setLanguage(String language) {
        if (context.getEngine().getLanguages().containsKey(language)) {
            Value bindings;
            try {
                bindings = context.getBindings(language);
            } catch (Exception e) {
                return "Failed to get bindings";
            }
            Value finalBindings = bindings;
            members.forEach((k, v) -> {
                try {
                    if (!finalBindings.hasMember(k)) {
                        finalBindings.putMember(k, v);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            this.language = language;
            return null;
        } else {
            return "Language not found.\n" +
                    "Available languages: \u00A7r" + context.getEngine().getLanguages().keySet();
        }
    }

    @Override
    public void setObject(String name, Object object) {
        members.put(name, object);
    }
}
