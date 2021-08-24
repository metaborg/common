package mb.common.editor;

import mb.common.region.Region;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;

/**
 * Represents the result of a hover operation. If such a result exists,
 * it means the language implementation was able to find an appropriate
 * hover tooltip for a specific section of the document.
 */
public final class HoverResult implements Serializable {
    /**
     * The region that this hover should be attached to. Some platforms
     * may highlight this area.
     */
    private final Region region;

    /**
     * The text to be shown on the hover tooltip. Some platforms may support
     * custom markup, such as HTML (eclipse) or Markdown.
     */
    private final String text;

    public HoverResult(Region highlightedRegion, String text) {
        this.region = highlightedRegion;
        this.text = text;
    }

    /**
     * @return the region that this hover text applies to
     */
    public Region getRegion() {
        return region;
    }

    /**
     * @return the text to be shown
     */
    public String getText() {
        return text;
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        HoverResult that = (HoverResult)o;

        if(!region.equals(that.region)) return false;
        return text.equals(that.text);
    }

    @Override public int hashCode() {
        int result = region.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }

    @Override public String toString() {
        return "HoverResult{" +
            "region=" + region +
            ", text='" + text + '\'' +
            '}';
    }
}
