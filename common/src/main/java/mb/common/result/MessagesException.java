package mb.common.result;

import mb.common.message.HasMessages;
import mb.common.message.KeyedMessages;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class MessagesException extends Exception implements HasMessages {
    private final KeyedMessages messages;


    public MessagesException(KeyedMessages messages, @Nullable String description, @Nullable Throwable cause, boolean createStackTrace, boolean enableSuppression) {
        super(description, cause, enableSuppression, createStackTrace);
        this.messages = messages;
    }

    public MessagesException(KeyedMessages messages, @Nullable String description, @Nullable Throwable cause, boolean createStackTrace) {
        this(messages, description, cause, createStackTrace, true);
    }

    public MessagesException(KeyedMessages messages, @Nullable String description, @Nullable Throwable cause) {
        this(messages, description, cause, false /* By default, no stacktrace*/);
    }

    public MessagesException(KeyedMessages messages, @Nullable String description) {
        this(messages, description, null);
    }

    public MessagesException(KeyedMessages messages) {
        this(messages, null);
    }

    public MessagesException(@Nullable String description, @Nullable Throwable cause) {
        this(KeyedMessages.of(), description, cause);
    }

    public MessagesException(@Nullable String description) {
        this(KeyedMessages.of(), description);
    }

    public MessagesException() {
        this(KeyedMessages.of());
    }


    public static MessagesException withStackTrace(KeyedMessages messages) {
        return new MessagesException(messages);
    }

    public static MessagesException withStackTrace(KeyedMessages messages, @Nullable String description) {
        return new MessagesException(messages, description);
    }

    public static MessagesException withStackTrace(KeyedMessages messages, @Nullable String description, @Nullable Throwable cause) {
        return new MessagesException(messages, description, cause);
    }


    @Override public KeyedMessages getMessages() {
        return messages;
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final MessagesException that = (MessagesException)o;
        return messages.equals(that.messages);
    }

    @Override public int hashCode() {
        return Objects.hash(messages);
    }

    @Override public String toString() {
        return "MessagesException(" + messages + ")";
    }
}
