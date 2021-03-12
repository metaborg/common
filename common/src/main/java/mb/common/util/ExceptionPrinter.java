package mb.common.util;

import mb.common.message.KeyedMessages;
import mb.common.message.Message;
import mb.resource.ReadableResource;
import mb.resource.ResourceKey;
import mb.resource.fs.FSPath;
import mb.resource.fs.FSResource;
import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.ResourcePath;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ExceptionPrinter {
    private boolean printStackTrace = true;

    private final ArrayList<FindAndReplace> findAndReplace = new ArrayList<>();
    private boolean hasContext = false;


    public void printException(Throwable throwable, PrintStream printStream) {
        printContext(printStream);
        printException(throwable, throwable, printStream, 0);
    }

    public String printExceptionToString(Throwable throwable, Charset charset) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try(final PrintStream printStream = new PrintStream(outputStream, false, charset.name())) {
            printException(throwable, printStream);
            printStream.flush();
        } catch(UnsupportedEncodingException e) {
            // Cannot happen because charset name is from a Charset. Rethrow as runtime exception just in case.
            throw new RuntimeException(e);
        }
        try {
            return outputStream.toString(charset.name());
        } catch(UnsupportedEncodingException e) {
            // Cannot happen because charset name is from a Charset. Rethrow as runtime exception just in case.
            throw new RuntimeException(e);
        }
    }

    public String printExceptionToString(Throwable throwable) {
        return printExceptionToString(throwable, StandardCharsets.UTF_8);
    }


    public void setPrintStackTrace(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }


    public void addContext(String name, String find, String replace) {
        this.findAndReplace.add(new FindAndReplace(name, find, replace));
        this.hasContext = true;
    }

    public void addFindAndReplace(String find, String replace) {
        this.findAndReplace.add(new FindAndReplace(null, find, replace));
    }


    public void addCurrentDirectoryContext(FSResource directory) {
        addCurrentDirectoryContext(directory.getPath());
    }

    public void addCurrentDirectoryContext(FSPath directory) {
        addFindAndReplace(directory.toString(), "./");
        addCurrentDirectoryContext(directory.getJavaPath().toString());
    }

    public void addCurrentDirectoryContext(HierarchicalResource directory) {
        addCurrentDirectoryContext(directory.getPath());
    }

    public void addCurrentDirectoryContext(ResourcePath directory) {
        if(directory instanceof FSPath) {
            addCurrentDirectoryContext((FSPath)directory);
        } else {
            addFindAndReplace(directory.toString(), "./");
            addCurrentDirectoryContext(directory.getIdAsString());
        }
    }

    public void addCurrentDirectoryContext(String directoryPath) {
        final String directoryStrWithSlash;
        final String directoryStrWithoutSlash;
        if(directoryPath.endsWith("/") || directoryPath.endsWith("\\")) {
            directoryStrWithSlash = directoryPath;
            directoryStrWithoutSlash = directoryPath.substring(0, directoryPath.length() - 1);
        } else {
            directoryStrWithSlash = directoryPath + "/";
            directoryStrWithoutSlash = directoryPath;
        }
        addContext("currentDir", directoryStrWithSlash, "./");
        addFindAndReplace(directoryStrWithoutSlash, "./");
        addFindAndReplace(".//", "./");
    }


    private void printContext(PrintStream printStream) {
        if(!hasContext) return;
        printStream.println("Printing exception with context:");
        for(FindAndReplace findAndReplace : this.findAndReplace) {
            if(findAndReplace.name == null) continue;
            printIndentation(printStream, 1);
            printStream.print(findAndReplace.name);
            printStream.print(": ");
            printStream.print(findAndReplace.replace);
            printStream.print(" = ");
            printStream.print(findAndReplace.find);
            printStream.println();
        }
    }

    private void printException(Throwable lastParentWithStackTrace, Throwable current, PrintStream printStream, int indentation) {
        printThrowableMessage(current, printStream);
        if(printStackTrace) {
            ThrowableUtil.getStackTrace(current).ifPresent(st -> printStackTrace(lastParentWithStackTrace, current, st, printStream, indentation));
        }
        ThrowableUtil.getMessages(current).ifPresent(m -> printMessages(lastParentWithStackTrace, current, m, printStream, indentation));
        ThrowableUtil.getSuppressed(current).ifPresent(s -> printSuppressed(lastParentWithStackTrace, current, s, printStream, indentation));
        ThrowableUtil.getCause(current).ifPresent(c -> printCause(lastParentWithStackTrace, current, c, printStream, indentation));
    }

    private void printThrowableMessage(Throwable current, PrintStream printStream) {
        printStream.print(current.getClass().getName());
        final @Nullable String message = current.getMessage();
        if(!StringUtil.isBlank(message)) {
            printStream.print(": ");
            printStream.print(findAndReplace(message));
        }
        printStream.println();
    }

    private void printStackTrace(Throwable lastParentWithStackTrace, Throwable current, StackTraceElement[] trace, PrintStream printStream, int indentation) {
        if(lastParentWithStackTrace != current /* Reference equality intended */) {
            final StackTraceElement[] parentTrace = lastParentWithStackTrace.getStackTrace();
            int m = trace.length - 1;
            int n = parentTrace.length - 1;
            while(m >= 0 && n >= 0 && trace[m].equals(parentTrace[n])) {
                m--;
                n--;
            }
            int framesInCommon = trace.length - 1 - m;
            for(int i = 0; i <= m; i++) {
                printIndentation(printStream, indentation + 1);
                printStream.print("at ");
                printStream.println(trace[i]);
            }
            if(framesInCommon != 0) {
                printIndentation(printStream, indentation + 1);
                printStream.println("... " + framesInCommon + " more in common with parent stacktrace");
            }
        } else {
            for(StackTraceElement element : trace) {
                printIndentation(printStream, indentation + 1);
                printStream.print("at ");
                printStream.println(element);
            }
        }
    }

    private void printMessages(Throwable lastParentWithStackTrace, Throwable current, KeyedMessages allMessages, PrintStream printStream, int indentation) {
        allMessages.getMessagesWithKey().forEach((resource, messages) -> {
            if(!messages.isEmpty()) {
                printIndentation(printStream, indentation);
                printStream.print("Messages of ");
                printStream.print(findAndReplace(resource.toString()));
                printStream.print(":");
                printStream.println();
                messages.forEach(m -> printMessage(lastParentWithStackTrace, current, m, printStream, indentation));
            }
        });
        if(!allMessages.getMessagesWithoutKey().isEmpty()) {
            final @Nullable ResourceKey resource = allMessages.getResourceForMessagesWithoutKeys();
            if(resource != null) {
                printIndentation(printStream, indentation);
                printStream.print("Messages of ");
                printStream.print(findAndReplace(resource.toString()));
                printStream.print(":");
                printStream.println();
            } else {
                printIndentation(printStream, indentation);
                printStream.print("Messages of ");
                printStream.println("(unknown origin):");
            }
            allMessages.getMessagesWithoutKey().forEach(m -> printMessage(lastParentWithStackTrace, current, m, printStream, indentation));
        }
    }

    private void printMessage(Throwable lastParentWithStackTrace, Throwable current, Message message, PrintStream printStream, int indentation) {
        printIndentation(printStream, indentation + 1);
        printStream.print(message.severity);
        if(message.region != null) {
            printStream.print(' ');
            printStream.print(message.region.toString());
        }
        printStream.print(": ");
        printStream.print(findAndReplace(message.text));
        printStream.println();
        if(message.exception != null) {
            printIndentation(printStream, indentation + 1);
            printStream.print("Exception of message: ");
            printNestedException(lastParentWithStackTrace, current, message.exception, printStream, indentation + 1);
        }
    }

    private void printSuppressed(Throwable lastParentWithStackTrace, Throwable current, Throwable[] allSuppressed, PrintStream printStream, int indentation) {
        for(Throwable suppressed : allSuppressed) {
            printIndentation(printStream, indentation);
            printStream.print("Suppressed: ");
            printNestedException(lastParentWithStackTrace, current, suppressed, printStream, indentation);
        }
    }

    private void printCause(Throwable lastParentWithStackTrace, Throwable current, Throwable cause, PrintStream printStream, int indentation) {
        printIndentation(printStream, indentation);
        printStream.print("Caused by: ");
        printNestedException(lastParentWithStackTrace, current, cause, printStream, indentation);
    }

    private void printNestedException(Throwable lastParentWithStackTrace, Throwable current, Throwable nested, PrintStream printStream, int indentation) {
        // TODO: prevent infinite loops with dejavu collection, like Throwable.printStackTrace does.
        final Throwable newParentWithStackTrace;
        if(ThrowableUtil.getStackTrace(current).isPresent()) {
            newParentWithStackTrace = current;
        } else {
            newParentWithStackTrace = lastParentWithStackTrace;
        }
        printException(newParentWithStackTrace, nested, printStream, indentation);
    }


    private void printIndentation(PrintStream printStream, int indentation) {
        printStream.print(StringUtil.repeat("  ", Integer.max(indentation, 0)));
    }

    private static class FindAndReplace {
        public final @Nullable String name;
        public final String find;
        public final String replace;

        public FindAndReplace(@Nullable String name, String find, String replace) {
            this.name = name;
            this.find = find;
            this.replace = replace;
        }
    }

    private String findAndReplace(String str) {
        for(FindAndReplace findAndReplace : this.findAndReplace) {
            str = str.replace(findAndReplace.find, findAndReplace.replace);
        }
        return str;
    }
}
