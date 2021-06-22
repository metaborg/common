package mb.common.text;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;

/**
 * A textual fragment at a certain start offset in the source code.
 */
public class StringFragment implements Serializable {
    public final int startOffset;
    public final String text;

    public StringFragment(int startOffset, String text) {
        this.startOffset = startOffset;
        this.text = text;
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final StringFragment stringFragment = (StringFragment)o;
        if(startOffset != stringFragment.startOffset) return false;
        return text.equals(stringFragment.text);
    }

    @Override public int hashCode() {
        int result = startOffset;
        result = 31 * result + text.hashCode();
        return result;
    }

    @Override public String toString() {
        return "Fragment{" +
            "startOffset=" + startOffset +
            ", text='" + text + '\'' +
            '}';
    }
}
