package uwu.smsgamer.serverscripter.graalvm.scripts;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import uwu.smsgamer.serverscripter.scripts.Script;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

// TODO: Fix ruby support
// TODO: Figure out how LLVM works so we can use it
public class GVMScript extends Script {
    private String language;
    private Context context;
    private Source source;

    public GVMScript(File scriptFile, String language) throws IOException {
        super(scriptFile);
        this.language = language;
        context = Context.newBuilder().allowAllAccess(true).build();
        source = Source.newBuilder(language, scriptFile).build();
    }

    @Override
    protected void loadScript() {
        System.out.println("LoadScript: " + scriptFile.getName());
        context.eval(source);
    }

    @Override
    protected void unloadScript() {
        System.out.println("UnloadScript: " + scriptFile.getName());
        disable();
        context.close();
    }

    private static final List<String> onEnableFunctionNames = Collections.unmodifiableList(
            Arrays.asList(
                    "onEnable",
                    "on_enable",
                    "OnEnable",
                    "On_Enable"
            )
    );

    private static final List<String> onDisableFunctionNames = Collections.unmodifiableList(
            Arrays.asList(
                    "onDisable",
                    "on_disable",
                    "OnDisable",
                    "On_Disable"
            )
    );

    private static final List<String> onReloadFunctionNames = Collections.unmodifiableList(
            Arrays.asList(
                    "onReload",
                    "on_reload",
                    "OnReload",
                    "On_Reload"
            )
    );

    private void tryExecuteFunctions(List<String> functionNames) {
        switch (language) {
            case "ruby": {
                Value bindings = context.getBindings(language);
                Set<String> members = bindings.getMemberKeys();
                for (String memberKey : members) {
                    Value member = bindings.getMember(memberKey);
                    if (functionNames.contains(memberKey)) {
                        if (member.canExecute()) {
                                bindings.getMember(memberKey).execute();
                                System.out.println("Executed function: " + memberKey);
                        }
                    } else {
                        if (member != null && member.hasMembers()) {
                            for (String memberKey2 : member.getMemberKeys()) {
                                Value member2 = member.getMember(memberKey2);
                                if (functionNames.contains(memberKey2)) {
                                    if (member2.canExecute()) {
                                        bindings.getMember(memberKey2).execute();
                                        System.out.println("Executed function: " + memberKey + "." + memberKey2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            default: {
                Value bindings = context.getBindings(language);
                Set<String> members = bindings.getMemberKeys();
                for (String member : members) {
                    if (functionNames.contains(member)) {
                        Value function = bindings.getMember(member);
                        if (function.canExecute()) {
                            try {
                                function.execute();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        } else {
                            System.out.println("Function " + member + " is not executable! File: " + scriptFile.getName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void enable() {
        System.out.println("EnableScript: " + scriptFile.getName());
        tryExecuteFunctions(onEnableFunctionNames);
    }

    @Override
    public void disable() {
        System.out.println("DisableScript: " + scriptFile.getName());
        tryExecuteFunctions(onDisableFunctionNames);
    }

    @Override
    public void reload() {
        System.out.println("ReloadScript: " + scriptFile.getName());
        tryExecuteFunctions(onReloadFunctionNames);
    }

    @Override
    public void setObject(String name, Object object) {
        context.getBindings(language).putMember(name, object);
    }

    @Override
    public Object getObject(String name) {
        return context.getBindings(language).getMember(name);
    }
}
