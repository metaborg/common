package mb.common.util;

import mb.common.option.Option;

import java.util.stream.Stream;

public class StreamUtil {
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public static <T> Option<T> findOne(Stream<T> stream) {
        final boolean[] one = {true};
        final Object[] elem = new Object[]{null};
        stream.forEach(e -> {
            if(elem[0] == null) {
                elem[0] = e;
            } else {
                one[0] = false;
            }
        });
        if(elem[0] == null || !one[0]) return Option.ofNone();
        else return Option.ofSome((T)elem[0]);
    }
}
