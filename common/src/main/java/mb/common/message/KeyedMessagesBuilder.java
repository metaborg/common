package mb.common.message;

import mb.common.region.Region;
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

    public KeyedMessagesBuilder addMessage(Message message, ResourceKey resourceKey) {
        this.messages.put(resourceKey, message);
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

    public KeyedMessagesBuilder addMessages(ResourceKey resourceKey, Iterable<? extends Message> messages) {
        this.messages.putAll(resourceKey, messages);
        return this;
    }

    public KeyedMessagesBuilder addMessages(ResourceKey resourceKey, Messages messages) {
        this.messages.putAll(resourceKey, messages.messages);
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

    public KeyedMessagesBuilder addMessagesWithFallbackKey(KeyedMessages keyedMessages, ResourceKey fallbackKey) {
        for(Map.Entry<ResourceKey, ArrayList<Message>> entry : keyedMessages.messages) {
            this.messages.putAll(entry.getKey(), entry.getValue());
        }
        final ResourceKey defaultKey = keyedMessages.resourceForMessagesWithoutKeys != null ? keyedMessages.resourceForMessagesWithoutKeys : fallbackKey;
        this.messages.putAll(defaultKey, keyedMessages.messagesWithoutKey);
        return this;
    }

    public KeyedMessagesBuilder addMessages(KeyedMessagesBuilder keyedMessagesBuilder) {
        this.messages.putAll(keyedMessagesBuilder.messages);
        this.messagesWithoutKey.addAll(keyedMessagesBuilder.messagesWithoutKey);
        return this;
    }

    public KeyedMessagesBuilder addMessagesWithDefaultKey(KeyedMessagesBuilder keyedMessagesBuilder, ResourceKey defaultKey) {
        this.messages.putAll(keyedMessagesBuilder.messages);
        this.messages.putAll(defaultKey, keyedMessagesBuilder.messagesWithoutKey);
        return this;
    }


    public KeyedMessagesBuilder extractMessages(Object object) {
        if(object instanceof HasMessages) {
            final HasMessages hasMessages = (HasMessages)object;
            final KeyedMessages messages = hasMessages.getMessages();
            addMessages(messages);
        } else if(object instanceof HasOptionalMessages) {
            final HasOptionalMessages hasOptionalMessages = (HasOptionalMessages)object;
            hasOptionalMessages.getOptionalMessages().ifPresent(this::addMessages);
        }
        return this;
    }

    public KeyedMessagesBuilder extractMessagesWithFallbackKey(Object object, ResourceKey fallbackKey) {
        if(object instanceof HasMessages) {
            final HasMessages hasMessages = (HasMessages)object;
            final KeyedMessages messages = hasMessages.getMessages();
            addMessagesWithFallbackKey(messages, fallbackKey);
        } else if(object instanceof HasOptionalMessages) {
            final HasOptionalMessages hasOptionalMessages = (HasOptionalMessages)object;
            hasOptionalMessages.getOptionalMessages().ifPresent(m -> addMessagesWithFallbackKey(m, fallbackKey));
        }
        return this;
    }

    public KeyedMessagesBuilder extractMessagesRecursively(Throwable throwable) {
        extractMessages(throwable);
        final @Nullable Throwable cause = throwable.getCause();
        if(cause != null && cause != throwable /* Reference equality intended */) {
            // TODO: prevent infinite loops with dejavu collection, like Throwable.printStackTrace does.
            // TODO: prevent infinite loops by only recursing a fixed number of times.
            extractMessagesRecursively(cause);
        }
        return this;
    }

    public KeyedMessagesBuilder extractMessagesRecursivelyWithFallbackKey(Throwable throwable, ResourceKey fallbackKey) {
        extractMessagesWithFallbackKey(throwable, fallbackKey);
        final @Nullable Throwable cause = throwable.getCause();
        if(cause != null && cause != throwable /* Reference equality intended */) {
            // TODO: prevent infinite loops with dejavu collection, like Throwable.printStackTrace does.
            // TODO: prevent infinite loops by only recursing a fixed number of times.
            extractMessagesRecursivelyWithFallbackKey(cause, fallbackKey);
        }
        return this;
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
