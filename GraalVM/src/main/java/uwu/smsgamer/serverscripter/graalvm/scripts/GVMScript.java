package uwu.smsgamer.serverscripter.graalvm.scripts;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import uwu.smsgamer.serverscripter.graalvm.GraalVMAddon;
import uwu.smsgamer.serverscripter.scripts.Script;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

// TODO: Fix ruby support
// TODO: Figure out how LLVM works so we can use it
public class GVMScript extends Script {
    private final String language;
    private final Context context;
    private final Source source;
    private final Value polyglotBindings;
    private final Value languageBindings;
    private final Logger logger;

    public GVMScript(File scriptFile, String language) throws IOException {
        super(scriptFile);
        logger = GraalVMAddon.getInstance().getLogger();
        this.language = language;
        context = Context.newBuilder()
                .allowAllAccess(true)
                .hostClassLoader(GraalVMAddon.class.getClassLoader())
                .build();
        source = Source.newBuilder(language, scriptFile).build();
        polyglotBindings = context.getPolyglotBindings();
        languageBindings = context.getBindings(language);
    }

    @Override
    protected void loadScript() {
        // Nothing to do here
    }

    @Override
    protected void unloadScript() {
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
            default: {
                Set<String> members = languageBindings.getMemberKeys();
                for (String member : members) {
                    if (functionNames.contains(member)) {
                        Value function = languageBindings.getMember(member);
                        if (function.canExecute()) {
                            try {
                                function.execute();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        } else {
                            logger.info("Function " + member + " is not executable! File: " + scriptFile.getName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void init() {
        super.init();
        logger.info("Init: " + scriptFile.getName());
        context.eval(source);
    }

    @Override
    public void enable() {
        logger.info("EnableScript: " + scriptFile.getName());
        tryExecuteFunctions(onEnableFunctionNames);
    }

    @Override
    public void disable() {
        logger.info("DisableScript: " + scriptFile.getName());
        tryExecuteFunctions(onDisableFunctionNames);
    }

    @Override
    public void reload() {
        logger.info("ReloadScript: " + scriptFile.getName());
        tryExecuteFunctions(onReloadFunctionNames);
    }

    @Override
    public void setObject(String name, Object object) {
        polyglotBindings.putMember(name, object);
        languageBindings.putMember(name, object);
    }

    @Override
    public Object getObject(String name) {
        if (languageBindings.hasMember(name)) {
            return languageBindings.getMember(name);
        }
        return polyglotBindings.getMember(name);
    }
}
