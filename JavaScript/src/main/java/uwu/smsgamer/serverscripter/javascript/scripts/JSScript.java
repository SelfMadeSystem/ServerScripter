package uwu.smsgamer.serverscripter.javascript.scripts;

import org.mozilla.javascript.*;
import uwu.smsgamer.serverscripter.scripts.Script;

import java.io.*;

public class JSScript extends Script {
    private final Context context;
    private final ScriptableObject scope;

    public JSScript(File scriptFile) {
        super(scriptFile);
        context = ContextFactory.getGlobal().enterContext();
        scope = context.initStandardObjects();
    }

    @Override
    protected void loadScript() {
        System.out.println("LoadScript: " + scriptFile.getName());
    }

    @Override
    public void init() {
        System.out.println("Init: " + scriptFile.getName());
        try {
            String name = getScriptFile().getName();
            int i = name.lastIndexOf(".");
            context.evaluateReader(scope, new FileReader(scriptFile), name.substring(0, i), 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enable() {
        System.out.println("Enable: " + scriptFile.getName());
        Object obj = scope.get("onEnable", scope);
        if (obj == Scriptable.NOT_FOUND || obj == null) return;
        Function function = (Function) obj;
        function.call(context, scope, scope, new Object[0]);
    }

    @Override
    public void disable() {
        System.out.println("Disable: " + scriptFile.getName());
        Object obj = scope.get("onDisable", scope);
        if (obj == Scriptable.NOT_FOUND || obj == null) return;
        Function function = (Function) obj;
        function.call(context, scope, scope, new Object[0]);
    }

    @Override
    public void reload() {
        System.out.println("Reload: " + scriptFile.getName());
        Object obj = scope.get("onReload", scope);
        if (obj == Scriptable.NOT_FOUND || obj == null) return;
        Function function = (Function) obj;
        function.call(context, scope, scope, new Object[0]);
    }
}
