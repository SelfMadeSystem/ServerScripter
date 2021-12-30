package uwu.smsgamer.serverscripter.python.scripts;

import org.python.core.*;
import org.python.util.PythonInterpreter;
import uwu.smsgamer.serverscripter.scripts.Script;

import java.io.*;
import java.util.*;

public class PyScript extends Script {
    public List<PyFunction> enableFuns = new ArrayList<>();
    public List<PyFunction> reloadFuns = new ArrayList<>();
    public List<PyFunction> disableFuns = new ArrayList<>();
    private final PythonInterpreter interpreter;

    public PyScript(File scriptFile) {
        super(scriptFile);
        interpreter = new PythonInterpreter();
    }

    @Override
    protected void loadScript() {
        System.out.println("LoadScript: " + scriptFile.getName());
        try {
            findScriptInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Finds script name, description, version, and author
    private void findScriptInfo() throws IOException { // FIXME: 12/30/21 This is a mess & it's not even working
        try {
            BufferedReader reader = new BufferedReader(new FileReader(scriptFile));
            boolean inLongComment = false;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    String[] split = line.substring(1).trim().split(" ");
                    if (split.length > 1) {
                        String key = split[1].toLowerCase();
                        if (key.endsWith(":")) {
                            key = key.substring(0, key.length() - 1);
                        }
                        String value = line.substring(line.indexOf(split[1]) + split[1].length() + 1);
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
                } else if (line.startsWith("\"\"\"") || inLongComment) {
                    if (inLongComment) inLongComment = !line.endsWith("\"\"\"");
                    else inLongComment = true;

                    String[] split;
                    if (line.startsWith("\"\"\"")) {
                        split = line.substring(3).trim().split(" ");
                    } else {
                        split = line.split(" ");
                    }

                    if (split.length > 1) {
                        String key = split[1].toLowerCase();
                        if (key.endsWith(":")) {
                            key = key.substring(0, key.length() - 1);
                        }
                        String value = line.substring(line.indexOf(split[1]) + split[1].length() + 1);
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
    public void init() {
        System.out.println("Init: " + scriptFile.getName());
        try {
            String name = getScriptFile().getName();
            int i = name.lastIndexOf(".");
            interpreter.execfile(new FileInputStream(getScriptFile()), name.substring(0, i));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getGoodFuns();
    }

    @Override
    public void enable() {
        System.out.println("Enable: " + scriptFile.getName());
        execAll(this.enableFuns);
    }

    @Override
    public void disable() {
        System.out.println("Disable: " + scriptFile.getName());
        execAll(this.disableFuns);
    }

    @Override
    public void reload() {
        System.out.println("Reload: " + scriptFile.getName());
        execAll(this.reloadFuns);
    }

    public void getGoodFuns() {
        getAndSet("onEnable", enableFuns);
        getAndSet("on_enable", enableFuns);
        getAndSet("enable", enableFuns);
        getAndSet("onReload", reloadFuns);
        getAndSet("on_reload", reloadFuns);
        getAndSet("reload", reloadFuns);
        getAndSet("onDisable", disableFuns);
        getAndSet("on_disable", disableFuns);
        getAndSet("disable", disableFuns);
    }

    private void getAndSet(String name, List<PyFunction> list) {
        PyObject obj = this.interpreter.get(name);
        if (obj instanceof PyFunction) list.add((PyFunction) obj);
    }

    public void execAll(List<PyFunction> funs) {
        for (PyFunction fun : funs) {
            fun.__call__();
        }
    }
}
