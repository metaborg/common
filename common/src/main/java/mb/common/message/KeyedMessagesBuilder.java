package mb.common.message;

import mb.common.region.Region;
import mb.common.util.IterableUtil;
import mb.common.util.ListView;
import mb.common.util.MultiMap;
import mb.common.util.MultiMapView;
import mb.resource.ResourceKey;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class KeyedMessagesBuilder {
    private final MultiMap<ResourceKey, Message> messages = MultiMap.withLinkedHash();
    private final ArrayList<Message> messagesWithoutKey = new ArrayList<>();


    public KeyedMessagesBuilder addMessage(String text, @Nullable Throwable exception, Severity severity, @Nullable ResourceKey resourceKey, @Nullable Region region) {
        final Message message = new Message(text, exception, severity, region);
        if(resourceKey != null) {
            messages.put(resourceKey, message);
        } else {
            messagesWithoutKey.add(message);
        }
        return this;
    }

    public KeyedMessagesBuilder addMessage(String text, Severity severity, @Nullable ResourceKey resourceKey, @Nullable Region region) {
        addMessage(text, null, severity, resourceKey, region);
        return this;
    }

    public KeyedMessagesBuilder addMessage(String text, @Nullable Throwable exception, Severity severity, @Nullable ResourceKey resourceKey) {
        addMessage(text, exception, severity, resourceKey, null);
        return this;
    }

    public KeyedMessagesBuilder addMessage(String text, Severity severity, @Nullable ResourceKey resourceKey) {
        addMessage(text, null, severity, resourceKey, null);
        return this;
    }

    public KeyedMessagesBuilder addMessage(String text, @Nullable Throwable exception, Severity severity, @Nullable Region region) {
        messagesWithoutKey.add(new Message(text, exception, severity, region));
        return this;
    }

    public KeyedMessagesBuilder addMessage(String text, @Nullable Throwable exception, Severity severity) {
        messagesWithoutKey.add(new Message(text, exception, severity, null));
        return this;
    }

    public KeyedMessagesBuilder addMessage(String text, Severity severity) {
        messagesWithoutKey.add(new Message(text, severity));
        return this;
    }

    public KeyedMessagesBuilder addMessage(Message message, @Nullable ResourceKey resourceKey) {
        if(resourceKey != null) {
            this.messages.put(resourceKey, message);
        } else {
            this.messagesWithoutKey.add(message);
        }
        return this;
    }

    public KeyedMessagesBuilder addMessage(Message message) {
        this.messagesWithoutKey.add(message);
        return this;
    }


    public KeyedMessagesBuilder addMessages(Collection<? extends Message> messages) {
        this.messagesWithoutKey.addAll(messages);
        return this;
    }

    public KeyedMessagesBuilder addMessages(@Nullable ResourceKey resourceKey, Iterable<? extends Message> messages) {
        if(resourceKey != null) {
            this.messages.putAll(resourceKey, messages);
        } else {
            IterableUtil.addAll(this.messagesWithoutKey, messages);
        }
        return this;
    }

    public KeyedMessagesBuilder addMessages(@Nullable ResourceKey resourceKey, Messages messages) {
        if(resourceKey != null) {
            this.messages.putAll(resourceKey, messages.messages);
        } else {
            messages.messages.addAllTo(this.messagesWithoutKey);
        }
        return this;
    }

    public KeyedMessagesBuilder addMessages(MultiMap<ResourceKey, Message> messages) {
        this.messages.putAll(messages);
        return this;
    }

    public KeyedMessagesBuilder addMessages(Messages messages) {
        messages.messages.addAllTo(this.messagesWithoutKey);
        return this;
    }

    public KeyedMessagesBuilder addMessages(KeyedMessages keyedMessages) {
        for(Map.Entry<ResourceKey, ArrayList<Message>> entry : keyedMessages.messages) {
            this.messages.putAll(entry.getKey(), entry.getValue());
        }
        if(keyedMessages.resourceForMessagesWithoutKeys != null) {
            this.messages.putAll(keyedMessages.resourceForMessagesWithoutKeys, keyedMessages.messagesWithoutKey);
        } else {
            keyedMessages.getMessagesWithoutKey().addAllTo(this.messagesWithoutKey);
        }
        return this;
    }

    public KeyedMessagesBuilder addMessagesWithFallbackKey(KeyedMessages keyedMessages, @Nullable ResourceKey fallbackKey) {
        for(Map.Entry<ResourceKey, ArrayList<Message>> entry : keyedMessages.messages) {
            this.messages.putAll(entry.getKey(), entry.getValue());
        }
        final @Nullable ResourceKey defaultKey = keyedMessages.resourceForMessagesWithoutKeys != null ? keyedMessages.resourceForMessagesWithoutKeys : fallbackKey;
        addMessages(defaultKey, keyedMessages.messagesWithoutKey);
        return this;
    }

    public KeyedMessagesBuilder addMessages(KeyedMessagesBuilder keyedMessagesBuilder) {
        this.messages.putAll(keyedMessagesBuilder.messages);
        this.messagesWithoutKey.addAll(keyedMessagesBuilder.messagesWithoutKey);
        return this;
    }

    public KeyedMessagesBuilder addMessagesWithDefaultKey(KeyedMessagesBuilder keyedMessagesBuilder, @Nullable ResourceKey defaultKey) {
        addMessagesWithFallbackKey(keyedMessagesBuilder.build(), defaultKey);
        return this;
    }


    public KeyedMessagesBuilder extractMessages(Object object) {
        internalExtractMessages(object);
        return this;
    }

    public KeyedMessagesBuilder extractMessagesWithFallbackKey(Object object, @Nullable ResourceKey fallbackKey) {
        internalExtractMessagesWithFallbackKey(object, fallbackKey);
        return this;
    }

    public KeyedMessagesBuilder extractMessagesRecursively(Throwable throwable) {
        final boolean found = internalExtractMessagesRecursively(throwable);
        if(!found) {
            // No messages found, add throwable itself as a message.
            addMessage(throwable.getMessage(), throwable, Severity.Error);
        }
        return this;
    }

    public KeyedMessagesBuilder extractMessagesRecursivelyWithFallbackKey(Throwable throwable, @Nullable ResourceKey fallbackKey) {
        final boolean found = internalExtractMessagesRecursivelyWithFallbackKey(throwable, fallbackKey);
        if(!found) {
            // No messages found, add throwable itself as a message.
            addMessage(throwable.getMessage(), throwable, Severity.Error, fallbackKey);
        }
        return this;
    }

    public boolean optionalExtractMessagesRecursivelyWithFallbackKey(Throwable throwable, @Nullable ResourceKey fallbackKey) {
        return internalExtractMessagesRecursivelyWithFallbackKey(throwable, fallbackKey);
    }


    private boolean internalExtractMessages(Object object) {
        if(object instanceof HasMessages) {
            final HasMessages hasMessages = (HasMessages)object;
            final KeyedMessages messages = hasMessages.getMessages();
            addMessages(messages);
            return true;
        } else if(object instanceof HasOptionalMessages) {
            final HasOptionalMessages hasOptionalMessages = (HasOptionalMessages)object;
            hasOptionalMessages.getOptionalMessages().ifPresent(this::addMessages);
            return hasOptionalMessages.getOptionalMessages().isPresent();
        }
        return false;
    }

    private boolean internalExtractMessagesWithFallbackKey(Object object, @Nullable ResourceKey fallbackKey) {
        if(object instanceof HasMessages) {
            final HasMessages hasMessages = (HasMessages)object;
            final KeyedMessages messages = hasMessages.getMessages();
            addMessagesWithFallbackKey(messages, fallbackKey);
            return true;
        } else if(object instanceof HasOptionalMessages) {
            final HasOptionalMessages hasOptionalMessages = (HasOptionalMessages)object;
            hasOptionalMessages.getOptionalMessages().ifPresent(m -> addMessagesWithFallbackKey(m, fallbackKey));
            return hasOptionalMessages.getOptionalMessages().isPresent();
        }
        return false;
    }

    private boolean internalExtractMessagesRecursively(Throwable throwable) {
        boolean found = internalExtractMessages(throwable);
        final @Nullable Throwable cause = throwable.getCause();
        if(cause != null && cause != throwable /* Reference equality intended */) {
            // TODO: prevent infinite loops with dejavu collection, like Throwable.printStackTrace does.
            // TODO: prevent infinite loops by only recursing a fixed number of times.
            found = found || internalExtractMessagesRecursively(cause);
        }
        return found;
    }

    private boolean internalExtractMessagesRecursivelyWithFallbackKey(Throwable throwable, @Nullable ResourceKey fallbackKey) {
        boolean found = internalExtractMessagesWithFallbackKey(throwable, fallbackKey);
        final @Nullable Throwable cause = throwable.getCause();
        if(cause != null && cause != throwable /* Reference equality intended */) {
            // TODO: prevent infinite loops with dejavu collection, like Throwable.printStackTrace does.
            // TODO: prevent infinite loops by only recursing a fixed number of times.
            found = found || internalExtractMessagesRecursivelyWithFallbackKey(cause, fallbackKey);
        }
        return found;
    }


    public KeyedMessagesBuilder replaceMessages(ResourceKey resourceKey, Iterable<? extends Message> messages) {
        this.messages.removeAll(resourceKey);
        this.messages.putAll(resourceKey, messages);
        return this;
    }

    public KeyedMessagesBuilder replaceMessages(ResourceKey resourceKey, ArrayList<Message> messages) {
        this.messages.replaceAll(resourceKey, messages);
        return this;
    }

    public KeyedMessagesBuilder replaceMessages(ResourceKey resourceKey, Messages messages) {
        this.messages.removeAll(resourceKey);
        this.messages.putAll(resourceKey, messages.messages);
        return this;
    }

    public KeyedMessagesBuilder replaceMessages(KeyedMessages keyedMessages) {
        this.messages.replaceAll(keyedMessages.messages.asUnmodifiable());
        return this;
    }


    public KeyedMessagesBuilder clear(ResourceKey resourceKey) {
        messages.removeAll(resourceKey);
        return this;
    }

    public KeyedMessagesBuilder clearWithoutKey() {
        messagesWithoutKey.clear();
        return this;
    }

    public KeyedMessagesBuilder clearAll() {
        messages.clear();
        messagesWithoutKey.clear();
        return this;
    }


    public KeyedMessages build(@Nullable ResourceKey resourceForMessagesWithoutKeys) {
        return new KeyedMessages(MultiMapView.copyOf(messages), ListView.copyOf(messagesWithoutKey), resourceForMessagesWithoutKeys);
    }

    public KeyedMessages build() {
        return build(null);
    }


    public boolean isEmpty() {
        return messagesWithoutKey.isEmpty() && messages.isEmpty();
    }
}
