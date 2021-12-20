package uwu.smsgamer.serverscripter.python.shell;

import org.python.util.PythonInterpreter;
import uwu.smsgamer.serverscripter.shell.PlayerOut;
import uwu.smsgamer.serverscripter.shell.PlayerShell;

import java.util.UUID;

public class PyPlayerShell extends PlayerShell {
    private final PythonInterpreter interpreter;
    protected PyPlayerShell(UUID uuid) {
        super(uuid);
        interpreter = new PythonInterpreter();
        interpreter.setOut(new PlayerOut(uuid, false));
        interpreter.setErr(new PlayerOut(uuid, true));
    }

    @Override
    public String execute(String command) {
        return interpreter.eval(command).toString();
    }
}
