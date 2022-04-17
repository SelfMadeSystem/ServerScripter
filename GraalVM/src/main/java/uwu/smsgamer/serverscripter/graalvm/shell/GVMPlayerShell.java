package uwu.smsgamer.serverscripter.graalvm.shell;

import org.graalvm.polyglot.*;
import uwu.smsgamer.serverscripter.graalvm.GraalVMAddon;
import uwu.smsgamer.serverscripter.shell.PlayerShell;
import uwu.smsgamer.serverscripter.shell.PlayerStream;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GVMPlayerShell extends PlayerShell {
    private String language;
    private final Context context;
    private final Value polyglotBindings;
    private Value languageBindings;
    private final Map<String, Object> values = new HashMap<>();

    protected GVMPlayerShell(UUID uuid) {
        super(uuid, GVMShell.getInstance());
        context = Context.newBuilder()
                .allowAllAccess(true)
                .hostClassLoader(GraalVMAddon.class.getClassLoader())
                .out(new PlayerStream(uuid, false))
                .err(new PlayerStream(uuid, true))
                .build();
        polyglotBindings = context.getPolyglotBindings();
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
        if (command.equals("__printAllBindings__")) {
            print("Polyglot bindings:" + polyglotBindings.getMemberKeys());
            print("Language bindings:" + languageBindings.getMemberKeys());
            return Result.EMPTY;
        }
        if (command.startsWith("__getBinding__")) {
            String[] split = command.split(" ");
            StringBuilder sb = new StringBuilder();
            if (split.length > 1) {
                int i = 1;
                Value value = polyglotBindings;
                while (i < split.length) {
                    if (sb.length() > 0) {
                        sb.append(".");
                    }
                    sb.append(split[i]);
                    if (!value.hasMembers()) {
                        printError("No such binding: " + sb);
                        return Result.EMPTY;
                    }
                    value = value.getMember(split[i]);
                    i++;
                }
                print(sb + ": " + value);
            }
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
            Value langBindings;
            try {
                langBindings = context.getBindings(language);
                if (langBindings != null) {
                    for (Map.Entry<String, Object> entry : values.entrySet()) {
                        langBindings.putMember(entry.getKey(), entry.getValue());
                    }
                }
            } catch (PolyglotException e) {
                return e.getMessage();
            }
            languageBindings = langBindings;
            this.language = language;
            return null;
        } else {
            return "Language not found.\n" +
                    "Available languages: \u00A7r" + context.getEngine().getLanguages().keySet();
        }
    }

    @Override
    public void setObject(String name, Object object) {
        polyglotBindings.putMember(name, object);
        values.put(name, object);
        if (languageBindings != null) {
            languageBindings.putMember(name, object);
        }
    }
}
