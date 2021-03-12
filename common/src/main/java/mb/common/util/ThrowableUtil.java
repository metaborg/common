package mb.common.util;

import mb.common.message.HasMessages;
import mb.common.message.HasOptionalMessages;
import mb.common.message.KeyedMessages;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

public class ThrowableUtil {
    public static Optional<StackTraceElement[]> getStackTrace(Throwable throwable) {
        final @Nullable StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        if(stackTraceElements != null && stackTraceElements.length != 0) {
            return Optional.of(stackTraceElements);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<KeyedMessages> getMessages(Throwable throwable) {
        final @Nullable KeyedMessages messages;
        if(throwable instanceof HasMessages) {
            messages = ((HasMessages)throwable).getMessages();
        } else if(throwable instanceof HasOptionalMessages) {
            messages = ((HasOptionalMessages)throwable).getOptionalMessages().orElse(null);
        } else {
            messages = null;
        }
        if(messages != null && !messages.isEmpty()) {
            return Optional.of(messages);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Throwable[]> getSuppressed(Throwable throwable) {
        final @Nullable Throwable[] suppressed = throwable.getSuppressed();
        if(suppressed != null && suppressed.length != 0) {
            return Optional.of(suppressed);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Throwable> getCause(Throwable throwable) {
        final @Nullable Throwable cause = throwable.getCause();
        if(cause != null && cause != throwable /* Reference equality intended */) {
            return Optional.of(cause);
        } else {
            return Optional.empty();
        }
    }
}
