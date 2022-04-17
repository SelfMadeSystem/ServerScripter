package uwu.smsgamer.serverscripter.javascript.scripts;

import org.mozilla.javascript.*;
import uwu.smsgamer.serverscripter.javascript.JSAddon;
import uwu.smsgamer.serverscripter.scripts.Script;

import java.io.*;
import java.util.logging.Logger;

public class JSScript extends Script {
    private final Context context;
    private final ScriptableObject scope;
    private final Logger logger;

    public JSScript(File scriptFile) {
        super(scriptFile);
        context = ContextFactory.getGlobal().enterContext();
        scope = context.initStandardObjects();
        logger = JSAddon.getInstance().getLogger();
    }

    @Override
    protected void loadScript() {
        logger.info("LoadScript: " + scriptFile.getName());
        try {
            findScriptInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Finds script name, description, version, and author
    private void findScriptInfo() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(scriptFile));
            boolean inLongComment = false;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("//")) {
                    String[] split = line.substring(2).trim().split(" ");
                    if (split.length > 1) {
                        String key = split[0].toLowerCase();
                        if (key.endsWith(":")) {
                            key = key.substring(0, key.length() - 1);
                        }
                        String value = line.substring(line.indexOf(split[0]) + split[0].length() + 1);
                        switch (key) {
                            case "name":
                                scriptName = value;
                                break;
                            case "description":
                                scriptDescription = value;
                                break;
                            case "version":
                                scriptVersion = value;
                                break;
                            case "author":
                                scriptAuthor = value;
                                break;
                        }
                    }
                } else if (line.startsWith("/*") || inLongComment) {
                    inLongComment = !line.endsWith("*/");
                    String[] split;
                    if (line.startsWith("/*")) {
                        split = line.substring(2).trim().split(" ");
                    } else if (line.startsWith("*")) {
                        split = line.substring(1).trim().split(" ");
                    } else {
                        split = line.split(" ");
                    }
                    if (split.length > 1) {
                        String key = split[0].toLowerCase();
                        if (key.endsWith(":")) {
                            key = key.substring(0, key.length() - 1);
                        }
                        String value = line.substring(line.indexOf(split[0]) + split[0].length() + 1);
                        switch (key) {
                            case "name":
                                scriptName = value;
                                break;
                            case "description":
                                scriptDescription = value;
                                break;
                            case "version":
                                scriptVersion = value;
                                break;
                            case "author":
                                scriptAuthor = value;
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Error finding script info: " + scriptFile.getName(), e);
        }
    }

    @Override
    protected void unloadScript() {
        logger.info("UnloadScript: " + scriptFile.getName());
        disable();
        synchronized (context) {
            Context.exit();
        }
    }

    @Override
    public void init() {
        if (initialized) return;
        super.init();
        logger.info("Init: " + scriptFile.getName());
        try {
            String name = getScriptFile().getName();
            int i = name.lastIndexOf(".");
            context.evaluateReader(scope, new FileReader(scriptFile), name.substring(0, i), 1, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enable() {
        logger.info("Enable: " + scriptFile.getName());
        Object obj = scope.get("onEnable", scope);
        if (obj == Scriptable.NOT_FOUND || obj == null) return;
        Function function = (Function) obj;
        function.call(context, scope, scope, new Object[0]);
    }

    @Override
    public void disable() {
        logger.info("Disable: " + scriptFile.getName());
        Object obj = scope.get("onDisable", scope);
        if (obj == Scriptable.NOT_FOUND || obj == null) return;
        Function function = (Function) obj;
        function.call(context, scope, scope, new Object[0]);
    }

    @Override
    public void reload() {
        logger.info("Reload: " + scriptFile.getName());
        Object obj = scope.get("onReload", scope);
        if (obj == Scriptable.NOT_FOUND || obj == null) return;
        Function function = (Function) obj;
        function.call(context, scope, scope, new Object[0]);
    }

    @Override
    public void setObject(String name, Object object) {
        scope.put(name, scope, object);
    }

    @Override
    public Object getObject(String name) {
        return scope.get(name, scope);
    }
}
