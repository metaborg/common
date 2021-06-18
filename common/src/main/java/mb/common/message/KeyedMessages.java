package mb.common.message;

import mb.common.util.ListView;
import mb.common.util.MultiMap;
import mb.common.util.MultiMapView;
import mb.common.util.SetView;
import mb.resource.ResourceKey;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class KeyedMessages implements Serializable {
    final MultiMapView<ResourceKey, Message> messages;
    final ListView<Message> messagesWithoutKey;
    final @Nullable ResourceKey resourceForMessagesWithoutKeys;


    public KeyedMessages(
        MultiMapView<ResourceKey, Message> messages,
        ListView<Message> messagesWithoutKey,
        @Nullable ResourceKey resourceForMessagesWithoutKeys
    ) {
        this.messages = messages;
        this.messagesWithoutKey = messagesWithoutKey;
        this.resourceForMessagesWithoutKeys = resourceForMessagesWithoutKeys;
    }

    public static KeyedMessages of() {
        return new KeyedMessages(MultiMapView.of(), ListView.of(), null);
    }

    public static KeyedMessages of(MultiMapView<ResourceKey, Message> messages, ListView<Message> messagesWithoutKey, ResourceKey resourceForMessagesWithoutKeys) {
        return new KeyedMessages(messages, messagesWithoutKey, resourceForMessagesWithoutKeys);
    }

    public static KeyedMessages of(MultiMapView<ResourceKey, Message> messages, ListView<Message> messagesWithoutKey) {
        return new KeyedMessages(messages, messagesWithoutKey, null);
    }

    public static KeyedMessages of(MultiMap<ResourceKey, Message> messages, ListView<Message> messagesWithoutKey, ResourceKey resourceForMessagesWithoutKeys) {
        return new KeyedMessages(MultiMapView.of(messages), messagesWithoutKey, resourceForMessagesWithoutKeys);
    }

    public static KeyedMessages of(MultiMap<ResourceKey, Message> messages, ListView<Message> messagesWithoutKey) {
        return new KeyedMessages(MultiMapView.of(messages), messagesWithoutKey, null);
    }

    public static KeyedMessages of(ListView<Message> messagesWithoutKey, ResourceKey resourceForMessagesWithoutKeys) {
        return new KeyedMessages(MultiMapView.of(), messagesWithoutKey, resourceForMessagesWithoutKeys);
    }

    public static KeyedMessages of(ListView<Message> messagesWithoutKey) {
        return new KeyedMessages(MultiMapView.of(), messagesWithoutKey, null);
    }

    public static KeyedMessages of(ArrayList<Message> messagesWithoutKey, ResourceKey resourceForMessagesWithoutKeys) {
        return new KeyedMessages(MultiMapView.of(), ListView.of(messagesWithoutKey), resourceForMessagesWithoutKeys);
    }

    public static KeyedMessages of(ArrayList<Message> messagesWithoutKey) {
        return new KeyedMessages(MultiMapView.of(), ListView.of(messagesWithoutKey), null);
    }

    public static KeyedMessages of(Messages messagesWithoutKey, ResourceKey resourceForMessagesWithoutKeys) {
        return new KeyedMessages(MultiMapView.of(), messagesWithoutKey.messages, resourceForMessagesWithoutKeys);
    }

    public static KeyedMessages of(Messages messagesWithoutKey) {
        return new KeyedMessages(MultiMapView.of(), messagesWithoutKey.messages, null);
    }

    public static KeyedMessages of(HasMessages hasMessages) {
        return hasMessages.getMessages();
    }

    public static KeyedMessages of(HasOptionalMessages hasMessages) {
        return hasMessages.getOptionalMessages().orElse(KeyedMessages.of());
    }


    public static KeyedMessages copyOf(MultiMap<ResourceKey, Message> keyedMessages, Collection<Message> messagesWithoutKey, ResourceKey resourceForMessagesWithoutKeys) {
        return new KeyedMessages(MultiMapView.copyOf(keyedMessages), ListView.copyOf(messagesWithoutKey), resourceForMessagesWithoutKeys);
    }

    public static KeyedMessages copyOf(MultiMap<ResourceKey, Message> keyedMessages, Collection<Message> messagesWithoutKey) {
        return new KeyedMessages(MultiMapView.copyOf(keyedMessages), ListView.copyOf(messagesWithoutKey), null);
    }

    public static KeyedMessages copyOf(MultiMap<ResourceKey, Message> keyedMessages) {
        return KeyedMessages.copyOf(keyedMessages, new ArrayList<>());
    }

    public static KeyedMessages copyOf(Collection<Message> messagesWithoutKey, ResourceKey resourceForMessagesWithoutKeys) {
        return new KeyedMessages(MultiMapView.of(), ListView.copyOf(messagesWithoutKey), resourceForMessagesWithoutKeys);
    }

    public static KeyedMessages copyOf(Collection<Message> messagesWithoutKey) {
        return new KeyedMessages(MultiMapView.of(), ListView.copyOf(messagesWithoutKey), null);
    }

    public static KeyedMessages copyOf(KeyedMessages keyedMessages) {
        return new KeyedMessages(MultiMapView.copyOf(keyedMessages.messages), ListView.copyOf(keyedMessages.messagesWithoutKey), keyedMessages.resourceForMessagesWithoutKeys);
    }

    public static KeyedMessages copyOf(HasMessages hasMessages) {
        return KeyedMessages.copyOf(hasMessages.getMessages());
    }


    public static Optional<KeyedMessages> ofTryExtractMessagesFrom(Object object, @Nullable ResourceKey resourceForMessagesWithoutKeys) {
        if(object instanceof HasMessages) {
            final HasMessages hasMessages = (HasMessages)object;
            final KeyedMessages messages = hasMessages.getMessages();
            if(resourceForMessagesWithoutKeys != null) {
                messages.withResourceForMessagesWithoutKeys(resourceForMessagesWithoutKeys);
            }
            return Optional.of(messages);
        } else if(object instanceof HasOptionalMessages) {
            final HasOptionalMessages hasOptionalMessages = (HasOptionalMessages)object;
            Optional<KeyedMessages> messages = hasOptionalMessages.getOptionalMessages();
            if(resourceForMessagesWithoutKeys != null) {
                messages = messages.map(k -> k.withResourceForMessagesWithoutKeys(resourceForMessagesWithoutKeys));
            }
            return messages;
        } else {
            return Optional.empty();
        }
    }

    public static Optional<KeyedMessages> ofTryExtractMessagesFrom(Object object) {
        return ofTryExtractMessagesFrom(object, null);
    }

    public static Optional<KeyedMessages> copyOfTryExtractMessagesFrom(Object object, @Nullable ResourceKey resourceForMessagesWithoutKeys) {
        return ofTryExtractMessagesFrom(object, resourceForMessagesWithoutKeys).map(KeyedMessages::copyOf);
    }

    public static Optional<KeyedMessages> copyOfTryExtractMessagesFrom(Object object) {
        return copyOfTryExtractMessagesFrom(object, null);
    }


    public KeyedMessages withResourceForMessagesWithoutKeys(ResourceKey resource) {
        return new KeyedMessages(messages, messagesWithoutKey, resource);
    }


    public int size() {
        return messages.stream()
            .map(Map.Entry::getValue)
            .mapToInt(ArrayList::size)
            .sum()
            + messagesWithoutKey.size();
    }

    public boolean isEmpty() {
        return messages.isEmpty() && messagesWithoutKey.isEmpty();
    }


    public ListView<Message> getMessagesOfKey(ResourceKey resource) {
        final ArrayList<Message> messagesForKey = messages.get(resource);
        return ListView.of(messagesForKey);
    }

    public MultiMapView<ResourceKey, Message> getMessagesWithKey() {
        return messages;
    }

    public SetView<ResourceKey> getKeys() {
        return messages.keySet();
    }


    public ListView<Message> getMessagesWithoutKey() {
        return messagesWithoutKey;
    }

    public @Nullable ResourceKey getResourceForMessagesWithoutKeys() {
        return resourceForMessagesWithoutKeys;
    }

    public Stream<Message> stream() {
        return Stream.concat(messages.values().stream().flatMap(Collection::stream), messagesWithoutKey.stream());
    }


    public boolean containsSeverity(Severity severity) {
        final boolean contains = messages.values().stream().flatMap(Collection::stream).anyMatch(
            message -> message.severity.equals(severity)
        );
        if(contains) return true;
        return messagesWithoutKey.stream().anyMatch(
            message -> message.severity.equals(severity)
        );
    }

    public boolean containsError() { return containsSeverity(Severity.Error); }

    public boolean containsWarning() { return containsSeverity(Severity.Warning); }

    public boolean containsInfo() { return containsSeverity(Severity.Info); }

    public boolean containsDebug() { return containsSeverity(Severity.Debug); }

    public boolean containsTrace() { return containsSeverity(Severity.Trace); }


    public Stream<Message> getMessagesOfSeverity(Severity severity) {
        return stream().filter(m -> m.severity.equals(severity));
    }

    public Stream<Message> getErrorMessages() { return getMessagesOfSeverity(Severity.Error); }

    public Stream<Message> getWarningMessages() { return getMessagesOfSeverity(Severity.Warning); }

    public Stream<Message> getInfoMessages() { return getMessagesOfSeverity(Severity.Info); }

    public Stream<Message> getDebugMessages() { return getMessagesOfSeverity(Severity.Debug); }

    public Stream<Message> getTraceMessages() { return getMessagesOfSeverity(Severity.Trace); }


    public boolean containsSeverityOrHigher(Severity severity) {
        final boolean contains = messages.values().stream().flatMap(Collection::stream).anyMatch(
            message -> message.severity.compareTo(severity) >= 0
        );
        if(contains) return true;
        return messagesWithoutKey.stream().anyMatch(
            message -> message.severity.compareTo(severity) >= 0
        );
    }

    public boolean containsErrorOrHigher() {
        return containsSeverityOrHigher(Severity.Error);
    }

    public boolean containsWarningOrHigher() {
        return containsSeverityOrHigher(Severity.Warning);
    }

    public boolean containsInfoOrHigher() {
        return containsSeverityOrHigher(Severity.Info);
    }

    public boolean containsDebugOrHigher() {
        return containsSeverityOrHigher(Severity.Debug);
    }

    public boolean containsTraceOrHigher() {
        return containsSeverityOrHigher(Severity.Trace);
    }


    public Messages asMessages() {
        final MessagesBuilder builder = new MessagesBuilder();
        for(ArrayList<Message> messages : messages.values()) {
            builder.addMessages(messages);
        }
        builder.addMessages(messagesWithoutKey);
        return builder.build();
    }


    public void addToStringBuilder(StringBuilder sb) {
        messages.forEachEntry((c, ms) -> {
            sb.append(c);
            sb.append(":\n");
            ms.forEach(m -> {
                sb.append("  ");
                sb.append(m.toString());
                if(m.exception != null) {
                    sb.append('\n');
                    final StringWriter stringWriter = new StringWriter();
                    m.exception.printStackTrace(new PrintWriter(stringWriter));
                    sb.append(stringWriter.toString());
                }
                sb.append('\n');
            });
        });
        if(messagesWithoutKey.isEmpty()) return;
        sb.append("\n\nmessages without key:\n");
        messagesWithoutKey.forEach((m) -> {
            sb.append(m.toString());
            sb.append('\n');
            if(m.exception != null) {
                sb.append('\n');
                final StringWriter stringWriter = new StringWriter();
                m.exception.printStackTrace(new PrintWriter(stringWriter));
                sb.append(stringWriter.toString());
            }
        });
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final KeyedMessages that = (KeyedMessages)o;
        return messages.equals(that.messages) &&
            messagesWithoutKey.equals(that.messagesWithoutKey);
    }

    @Override public int hashCode() {
        return Objects.hash(messages, messagesWithoutKey);
    }

    @Override public String toString() {
        return "KeyedMessages(" + (messages.size() + messagesWithoutKey.size()) + " messages)";
    }
}
