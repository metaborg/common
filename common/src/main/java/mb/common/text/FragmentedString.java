package mb.common.text;

import mb.common.util.ListView;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;

/**
 * A string that consists of multiple {@link StringFragment fragments} which may have different starting offsets in the
 * source code. An instance where this occurs is the SPT language where fragments of a language are embedded into the
 * SPT language.
 */
public class FragmentedString implements Serializable {
    public final ListView<StringFragment> fragments;

    public FragmentedString(ListView<StringFragment> fragments) {
        this.fragments = fragments;
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final FragmentedString that = (FragmentedString)o;
        return fragments.equals(that.fragments);
    }

    @Override public int hashCode() {
        return fragments.hashCode();
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder();
        for(StringFragment stringFragment : fragments) {
            sb.append(stringFragment.text);
        }
        return sb.toString();
    }
}
