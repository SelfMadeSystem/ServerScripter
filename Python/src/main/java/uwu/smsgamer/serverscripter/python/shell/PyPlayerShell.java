package uwu.smsgamer.serverscripter.python.shell;

import org.python.core.CompileMode;
import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.util.InteractiveInterpreter;
import uwu.smsgamer.serverscripter.shell.PlayerWriter;
import uwu.smsgamer.serverscripter.shell.PlayerShell;

import java.util.UUID;

public class PyPlayerShell extends PlayerShell {
    private final MyInteractiveInterpreter interpreter;
    protected PyPlayerShell(UUID uuid) {
        super(uuid, PyShell.getInstance());
        interpreter = new MyInteractiveInterpreter();
        interpreter.setOut(new PlayerWriter(uuid, false));
        interpreter.setErr(new PlayerWriter(uuid, true));
    }

    @Override
    public Result execute(String command) {
        PyObject compiled = interpreter.compile(command, "", CompileMode.exec);
        if (interpreter.isRunnable(compiled)) {
            try {
                interpreter.runcode(compiled);
            } catch (PyException e) {
                if (e.match(Py.SystemExit)) {
                    return Result.EXIT;
                }
            }
            return new Result(Result.Response.FINISHED, null);
        }
        return Result.UNFINISHED;
    }

    private static class MyInteractiveInterpreter extends InteractiveInterpreter {
        @Override
        public boolean runsource(String source, String filename, CompileMode kind) {
            PyObject code = Py.compile_command_flags(source, filename, kind, this.cflags, true);

            if (code == Py.None) {
                return true;
            } else {
                this.runcode(code);
                return false;
            }
        }

        public PyObject compile(String source, String filename, CompileMode kind) {
            return Py.compile_command_flags(source, filename, kind, this.cflags, true);
        }

        public boolean isRunnable(PyObject code) {
            return code != Py.None;
        }
    }
}
