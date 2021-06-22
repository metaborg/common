package mb.common.text;

import mb.common.util.ADT;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.Optional;

/**
 * Text, represented either by a regular {@link String}, or represented by a {@link FragmentedString fragmented string}
 * which has multiple fragments at different offsets.
 */
@ADT
public abstract class Text implements Serializable {
    interface Cases<R> {
        R string(String string);

        R fragmentedString(FragmentedString fragmentedString);
    }


    public static Text string(String string) {
        return Texts.string(string);
    }

    public static Text fragmentedString(FragmentedString fragmentedString) {
        return Texts.fragmentedString(fragmentedString);
    }


    public abstract <R> R match(Text.Cases<R> cases);

    public Optional<String> getString() {
        return Texts.getString(this);
    }

    public Optional<FragmentedString> getFragmentedString() {
        return Texts.getFragmentedString(this);
    }

    public static Texts.CasesMatchers.TotalMatcher_String cases() {
        return Texts.cases();
    }

    public Texts.CaseOfMatchers.TotalMatcher_String caseOf() {
        return Texts.caseOf(this);
    }


    @Override public abstract int hashCode();

    @Override public abstract boolean equals(@Nullable Object obj);

    @Override public String toString() {
        return caseOf().string(s -> s).fragmentedString(FragmentedString::toString);
    }
}
