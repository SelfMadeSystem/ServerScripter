package uwu.smsgamer.serverscripter.javascript.scripts;

import org.graalvm.polyglot.*;
import uwu.smsgamer.serverscripter.scripts.Script;

import java.io.*;

public class JSScript extends Script {
    private final Context context;
    private final Value bindings;

    public JSScript(File scriptFile) {
        super(scriptFile);
        context = Context.create();
        bindings = context.getBindings("js");
    }

    @Override
    protected void loadScript() {
        System.out.println("LoadScript: " + scriptFile.getName());
    }

    @Override
    public void init() {
        System.out.println("Init: " + scriptFile.getName());
        try {
            context.eval(Source.newBuilder("js", scriptFile).build());
            System.out.println(bindings + " : " + context.getBindings("js"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enable() {
        System.out.println("Enable: " + scriptFile.getName());
//        Object obj = bindings.get("onEnable", bindings);
//        if (obj == Scriptable.NOT_FOUND || obj == null) return;
//        Function function = (Function) obj;
//        function.call(context, bindings, bindings, new Object[0]);
    }

    @Override
    public void disable() {
        System.out.println("Disable: " + scriptFile.getName());
//        Object obj = bindings.get("onDisable", bindings);
//        if (obj == Scriptable.NOT_FOUND || obj == null) return;
//        Function function = (Function) obj;
//        function.call(context, bindings, bindings, new Object[0]);
    }

    @Override
    public void reload() {
        System.out.println("Reload: " + scriptFile.getName());
//        Object obj = bindings.get("onReload", bindings);
//        if (obj == Scriptable.NOT_FOUND || obj == null) return;
//        Function function = (Function) obj;
//        function.call(context, bindings, bindings, new Object[0]);
    }
}
