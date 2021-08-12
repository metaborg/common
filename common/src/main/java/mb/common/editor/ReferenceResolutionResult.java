package mb.common.editor;

import mb.common.region.Region;
import mb.common.util.ListView;
import mb.resource.ResourceKey;

import java.io.Serializable;

/**
 * Represents the result of a reference resolution operation. If such
 * a result exists, it means that the underlying (constraint) analyzer
 * was able to find at least one location in the project that is an
 * appropriate "reference" for the selected region.
 *
 * A reference resolution result carries the list of valid results (for
 * cases where more than one definition may apply), as well as the source
 * region in the file that should be highlighted. This allows the analyzer
 * to specify exactly which region should be highlighted by the IDE, since
 * the analyzed region is usually just an offset. In the cases where there
 * is only a single result, an IDE may jump to the definition directly. Some
 * platforms may not support more than one entry and default to the first.
 */
public final class ReferenceResolutionResult implements Serializable {
    /**
     * The region that should be highlighted and used as an anchor
     * point for the list of resolved references.
     */
    private final Region highlightedRegion;

    /**
     * The list of relevant references for this resolution operation.
     * This should contain at least one entry. In cases where no results
     * are found, the resolution should instead fail entirely.
     */
    private final ListView<ResolvedEntry> entries;

    public ReferenceResolutionResult(Region highlightedRegion, ListView<ResolvedEntry> entries) {
        this.highlightedRegion = highlightedRegion;
        this.entries = entries;
    }

    public Region getHighlightedRegion() {
        return highlightedRegion;
    }

    public ListView<ResolvedEntry> getEntries() {
        return entries;
    }

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        ReferenceResolutionResult that = (ReferenceResolutionResult)o;

        if(!highlightedRegion.equals(that.highlightedRegion)) return false;
        return entries.equals(that.entries);
    }

    @Override public int hashCode() {
        int result = highlightedRegion.hashCode();
        result = 31 * result + entries.hashCode();
        return result;
    }

    @Override public String toString() {
        return "ReferenceResolutionResult{" +
            "highlightedRegion=" + highlightedRegion +
            ", entries=" + entries +
            '}';
    }

    /**
     * Represents a single referenced location within a {@link ReferenceResolutionResult}.
     */
    public static final class ResolvedEntry implements Serializable {
        /**
         * The file that contains the {@link ResolvedEntry#region} of
         * this entry. A platform will usually navigate to this file if it is selected
         * by the user and resolves to some buffer that can be displayed.
         */
        private final ResourceKey file;

        /**
         * The region that should be highlighted in the target {@link ResolvedEntry#file}.
         * It is undefined behavior what happens if the region is not fully
         * contained within the file. In case you only require navigation to
         * the file, a zero-wide region at offset 0 will suffice.
         */
        private final Region region;

        /**
         * The label to be shown to the user. This label may not always be displayed,
         * as some platforms may not offer the ability to display custom labels. Some
         * platforms may only display labels if there is more than one result.
         */
        private final String label;

        public ResolvedEntry(ResourceKey file, Region region, String label) {
            this.file = file;
            this.region = region;
            this.label = label;
        }

        public ResourceKey getFile() {
            return file;
        }

        public Region getRegion() {
            return region;
        }

        public String getLabel() {
            return label;
        }

        @Override public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;

            ResolvedEntry that = (ResolvedEntry)o;

            if(!file.equals(that.file)) return false;
            if(!region.equals(that.region)) return false;
            return label.equals(that.label);
        }

        @Override public int hashCode() {
            int result = file.hashCode();
            result = 31 * result + region.hashCode();
            result = 31 * result + label.hashCode();
            return result;
        }

        @Override public String toString() {
            return "ResolvedEntry{" +
                "file=" + file +
                ", region=" + region +
                ", label='" + label + '\'' +
                '}';
        }
    }
}
