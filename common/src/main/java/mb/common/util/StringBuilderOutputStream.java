package mb.common.util;

import java.io.IOException;
import java.io.OutputStream;

public class StringBuilderOutputStream extends OutputStream {
    private final StringBuilder stringBuilder;

    public StringBuilderOutputStream(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override public void write(int b) throws IOException {
        stringBuilder.append((char)b);
    }
}
