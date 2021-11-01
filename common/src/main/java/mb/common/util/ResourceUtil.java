package mb.common.util;

import mb.resource.ReadableResource;
import mb.resource.WritableResource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public abstract class ResourceUtil {
    public static void copy(ReadableResource inputFile, WritableResource outputFile) throws IOException {
        try(
            final BufferedInputStream inputStream = inputFile.openReadBuffered();
            final BufferedOutputStream outputStream = outputFile.openWriteBuffered()
        ) {
            IOUtil.copy(inputStream, outputStream);
            outputStream.flush();
        }
    }
}
