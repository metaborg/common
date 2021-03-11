package mb.common.message;

import java.util.Optional;

public interface HasMessages extends HasOptionalMessages {
    KeyedMessages getMessages();

    @Override default Optional<KeyedMessages> getOptionalMessages() {
        return Optional.of(getMessages());
    }
}
