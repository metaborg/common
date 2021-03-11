package mb.common.message;

import java.util.Optional;

public interface HasOptionalMessages {
    Optional<KeyedMessages> getOptionalMessages();
}
