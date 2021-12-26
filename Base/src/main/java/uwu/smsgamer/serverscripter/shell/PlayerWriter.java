package uwu.smsgamer.serverscripter.shell;

import java.io.Writer;
import java.util.UUID;

public class PlayerWriter extends Writer {
    private final StringBuilder builder = new StringBuilder();
    protected final UUID uuid;
    protected final boolean error;

    public PlayerWriter(UUID uuid, boolean error) {
        this.uuid = uuid;
        this.error = error;
    }


    @Override
    public void write(char[] cbuf, int off, int len) {
        builder.append(cbuf, off, len);
    }

    @Override
    public void flush() {
        print(builder.toString());
        builder.setLength(0);
    }

    protected void print(String s) {
        if (this.error) {
            if (builder.length() > 0) {
                ShellManager.onPrintError.accept(this.uuid, s);
            }
        } else {
            if (builder.length() > 0) {
                ShellManager.onPrint.accept(this.uuid, s);
            }
        }
    }

    @Override
    public void close() {
        flush();
    }
}
