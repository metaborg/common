package mb.common.util;

import mb.common.message.HasMessages;
import mb.common.message.HasOptionalMessages;
import mb.common.message.KeyedMessages;
import mb.common.message.Message;
import mb.common.message.Severity;
import mb.resource.ResourceKey;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ExceptionPrinter {
    public static void printException(Throwable throwable, PrintStream printStream) {
        printStream.print(throwable.getClass().getName());
        final @Nullable String message = throwable.getMessage();
        if(!StringUtil.isBlank(message)) {
            printStream.print(": ");
            printStream.print(message);
        }
        final @Nullable StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        if(stackTraceElements != null && stackTraceElements.length != 0) {
            printStream.println();
            printStream.println("Stack trace:");
            for(StackTraceElement traceElement : stackTraceElements)
                printStream.println("\t" + traceElement);
        }
        final @Nullable KeyedMessages allMessages;
        if(throwable instanceof HasMessages) {
            allMessages = ((HasMessages)throwable).getMessages();
        } else if(throwable instanceof HasOptionalMessages) {
            allMessages = ((HasOptionalMessages)throwable).getOptionalMessages().orElse(null);
        } else {
            allMessages = null;
        }
        if(allMessages != null && !allMessages.isEmpty()) {
            printStream.println();
            printStream.println("Messages:");
            allMessages.getMessagesWithKey().forEach((resource, messages) -> {
                if(!messages.isEmpty()) {
                    printStream.print(resource.toString());
                    printStream.print(":");
                    printStream.println();
                    messages.forEach(m -> printMessage(m, printStream));
                }
            });
            if(!allMessages.getMessagesWithoutKey().isEmpty()) {
                final @Nullable ResourceKey resource = allMessages.getResourceForMessagesWithoutKeys();
                if(resource != null) {
                    printStream.print(resource.toString());
                    printStream.print(":");
                    printStream.println();
                } else {
                    printStream.println("(no originating resource):");
                }
                allMessages.getMessagesWithoutKey().forEach(m -> printMessage(m, printStream));
            }
        }
        final @Nullable Throwable[] suppressed = throwable.getSuppressed();
        if(suppressed != null && suppressed.length != 0) {
            printStream.println();
            printStream.println("Suppressed exceptions:");
            for(Throwable t : suppressed) {
                // TODO: add indentation.
                // TODO: remove common stack frames, like Throwable.printStackTrace does.
                printException(t, printStream);
            }
        }
        final @Nullable Throwable cause = throwable.getCause();
        if(cause != null && cause != throwable /* Reference equality intended */) {
            printStream.println();
            printStream.print("Caused by: ");
            // TODO: add indentation.
            // TODO: remove common stack frames, like Throwable.printStackTrace does.
            printException(cause, printStream);
        }
        // TODO: prevent infinite loops with dejavu collection, like Throwable.printStackTrace does.
        // TODO: prevent infinite loops by only recursing a fixed number of times.
    }

    public static String printExceptionToString(Throwable throwable, Charset charset) {
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

    public static String printExceptionToString(Throwable throwable) {
        return printExceptionToString(throwable, StandardCharsets.UTF_8);
    }


    private static void printMessage(Message message, PrintStream printStream) {
        printStream.print("  ");
        printStream.print(severityToString(message.severity));
        if(message.region != null) {
            printStream.print(' ');
            printStream.print(message.region.toString());
        }
        printStream.print(": ");
        printStream.print(message.text);
        printStream.println();
        // TODO: print nested exceptions.
    }

    private static String severityToString(Severity severity) {
        switch(severity) {
            case Trace: return "TRACE";
            case Debug: return "DEBUG";
            case Info: return " INFO";
            case Warning: return " WARN";
            case Error: return "ERROR";
            default: return "?????";
        }
    }
}
