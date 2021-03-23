package mb.common.message;

import mb.common.region.Region;
import mb.common.util.ListView;
import mb.common.util.MapView;
import mb.common.util.MultiMap;
import mb.resource.ResourceKey;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class KeyedMessagesBuilder {
    private final MultiMap<ResourceKey, Message> messages = MultiMap.withLinkedHash();
    private final ArrayList<Message> messagesWithoutKey = new ArrayList<>();


    public void addMessage(String text, @Nullable Throwable exception, Severity severity, @Nullable ResourceKey resourceKey, @Nullable Region region) {
        final Message message = new Message(text, exception, severity, region);
        if(resourceKey != null) {
            messages.put(resourceKey, message);
        } else {
            messagesWithoutKey.add(message);
        }
    }

    public void addMessage(String text, Severity severity, ResourceKey resourceKey, @Nullable Region region) {
        addMessage(text, null, severity, resourceKey, region);
    }

    public void addMessage(String text, @Nullable Throwable exception, Severity severity, @Nullable ResourceKey resourceKey) {
        addMessage(text, exception, severity, resourceKey, null);
    }

    public void addMessage(String text, Severity severity, @Nullable ResourceKey resourceKey) {
        addMessage(text, null, severity, resourceKey, null);
    }

    public void addMessage(String text, @Nullable Throwable exception, Severity severity, @Nullable Region region) {
        messagesWithoutKey.add(new Message(text, exception, severity, region));
    }

    public void addMessage(String text, @Nullable Throwable exception, Severity severity) {
        messagesWithoutKey.add(new Message(text, exception, severity, null));
    }

    public void addMessage(String text, Severity severity) {
        messagesWithoutKey.add(new Message(text, severity));
    }

    public void addMessage(Message message, ResourceKey resourceKey) {
        this.messages.put(resourceKey, message);
    }

    public void addMessage(Message message) {
        this.messagesWithoutKey.add(message);
    }


    public void addMessages(Collection<? extends Message> messages) {
        this.messagesWithoutKey.addAll(messages);
    }

    public void addMessages(ResourceKey resourceKey, Iterable<? extends Message> messages) {
        this.messages.putAll(resourceKey, messages);
    }

    public void addMessages(ResourceKey resourceKey, Messages messages) {
        this.messages.putAll(resourceKey, messages.messages);
    }

    public void addMessages(MultiMap<ResourceKey, Message> messages) {
        this.messages.putAll(messages);
    }

    public void addMessages(Messages messages) {
        messages.messages.addAllTo(this.messagesWithoutKey);
    }

    public void addMessages(KeyedMessages keyedMessages) {
        for(Map.Entry<ResourceKey, ArrayList<Message>> entry : keyedMessages.messages) {
            this.messages.putAll(entry.getKey(), entry.getValue());
        }
        if(keyedMessages.resourceForMessagesWithoutKeys != null) {
            this.messages.putAll(keyedMessages.resourceForMessagesWithoutKeys, keyedMessages.messagesWithoutKey);
        } else {
            keyedMessages.getMessagesWithoutKey().addAllTo(this.messagesWithoutKey);
        }
    }

    public void addMessagesWithFallbackKey(KeyedMessages keyedMessages, ResourceKey fallbackKey) {
        for(Map.Entry<ResourceKey, ArrayList<Message>> entry : keyedMessages.messages) {
            this.messages.putAll(entry.getKey(), entry.getValue());
        }
        final ResourceKey defaultKey = keyedMessages.resourceForMessagesWithoutKeys != null ? keyedMessages.resourceForMessagesWithoutKeys : fallbackKey;
        this.messages.putAll(defaultKey, keyedMessages.messagesWithoutKey);
    }

    public void addMessages(KeyedMessagesBuilder keyedMessagesBuilder) {
        this.messages.putAll(keyedMessagesBuilder.messages);
        this.messagesWithoutKey.addAll(keyedMessagesBuilder.messagesWithoutKey);
    }

    public void addMessagesWithDefaultKey(KeyedMessagesBuilder keyedMessagesBuilder, ResourceKey defaultKey) {
        this.messages.putAll(keyedMessagesBuilder.messages);
        this.messages.putAll(defaultKey, keyedMessagesBuilder.messagesWithoutKey);
    }


    public void addMessages(Object object) {
        if(object instanceof HasMessages) {
            final HasMessages hasMessages = (HasMessages)object;
            final KeyedMessages messages = hasMessages.getMessages();
            addMessages(messages);
        } else if(object instanceof HasOptionalMessages) {
            final HasOptionalMessages hasOptionalMessages = (HasOptionalMessages)object;
            hasOptionalMessages.getOptionalMessages().ifPresent(this::addMessages);
        }
    }

    public void addMessagesWithFallbackKey(Object object, ResourceKey fallbackKey) {
        if(object instanceof HasMessages) {
            final HasMessages hasMessages = (HasMessages)object;
            final KeyedMessages messages = hasMessages.getMessages();
            addMessagesWithFallbackKey(messages, fallbackKey);
        } else if(object instanceof HasOptionalMessages) {
            final HasOptionalMessages hasOptionalMessages = (HasOptionalMessages)object;
            hasOptionalMessages.getOptionalMessages().ifPresent(m -> addMessagesWithFallbackKey(m, fallbackKey));
        }
    }

    public void addMessagesRecursively(Throwable throwable) {
        addMessages(throwable);
        final @Nullable Throwable cause = throwable.getCause();
        if(cause != null && cause != throwable /* Reference equality intended */) {
            // TODO: prevent infinite loops with dejavu collection, like Throwable.printStackTrace does.
            // TODO: prevent infinite loops by only recursing a fixed number of times.
            addMessagesRecursively(cause);
        }
    }

    public void addMessagesRecursivelyWithFallbackKey(Throwable throwable, ResourceKey fallbackKey) {
        addMessagesWithFallbackKey(throwable, fallbackKey);
        final @Nullable Throwable cause = throwable.getCause();
        if(cause != null && cause != throwable /* Reference equality intended */) {
            // TODO: prevent infinite loops with dejavu collection, like Throwable.printStackTrace does.
            // TODO: prevent infinite loops by only recursing a fixed number of times.
            addMessagesRecursivelyWithFallbackKey(cause, fallbackKey);
        }
    }


    public void replaceMessages(ResourceKey resourceKey, Iterable<? extends Message> messages) {
        this.messages.removeAll(resourceKey);
        this.messages.putAll(resourceKey, messages);
    }

    public void replaceMessages(ResourceKey resourceKey, ArrayList<Message> messages) {
        this.messages.replaceAll(resourceKey, messages);
    }

    public void replaceMessages(ResourceKey resourceKey, Messages messages) {
        this.messages.removeAll(resourceKey);
        this.messages.putAll(resourceKey, messages.messages);
    }

    public void replaceMessages(KeyedMessages keyedMessages) {
        this.messages.replaceAll(keyedMessages.messages.asUnmodifiable());
    }


    public void clear(ResourceKey resourceKey) {
        messages.removeAll(resourceKey);
    }

    public void clearWithoutKey() {
        messagesWithoutKey.clear();
    }

    public void clearAll() {
        messages.clear();
        messagesWithoutKey.clear();
    }


    public KeyedMessages build(@Nullable ResourceKey resourceForMessagesWithoutKeys) {
        return new KeyedMessages(MapView.copyOfWithLinkedHash(messages.getInnerMap()), ListView.copyOf(messagesWithoutKey), resourceForMessagesWithoutKeys);
    }

    public KeyedMessages build() {
        return build(null);
    }


    public boolean isEmpty() {
        return messagesWithoutKey.isEmpty() && messages.values().stream().allMatch(ArrayList::isEmpty);
    }
}
