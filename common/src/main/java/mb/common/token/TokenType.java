package mb.common.token;

import mb.common.region.Region;
import mb.common.region.Selection;
import mb.common.region.Selections;
import mb.common.util.ADT;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;

@ADT
public abstract class TokenType implements Serializable {
    interface Cases<R> {
        R identifier();

        R string();

        R number();

        R keyword();

        R operator();

        R layout();

        R unknown();
    }

    public static TokenType identifier() {
        return TokenTypes.identifier();
    }

    public static TokenType string() {
        return TokenTypes.string();
    }

    public static TokenType number() {
        return TokenTypes.number();
    }

    public static TokenType keyword() {
        return TokenTypes.keyword();
    }

    public static TokenType operator() {
        return TokenTypes.operator();
    }

    public static TokenType layout() {
        return TokenTypes.layout();
    }

    public static TokenType unknown() {
        return TokenTypes.unknown();
    }


    public abstract <R> R match(Cases<R> cases);


    @Override public abstract int hashCode();

    @Override public abstract boolean equals(@Nullable Object obj);

    @Override public abstract String toString();
}
