package mb.common.codecompletion;

import mb.common.region.Region;
import mb.common.util.Experimental;
import mb.common.util.ListView;

import java.io.Serializable;
import java.util.Objects;

/**
 * The result of invoking completions.
 */
public class CodeCompletionResult implements Serializable {

    private static final CodeCompletionResult empty = new CodeCompletionResult(ListView.of(), Region.atOffset(0), true);
    public static CodeCompletionResult getEmpty() { return empty; }

    private final ListView<CodeCompletionItem> proposals;
    private final Region replacementRegion;
    private final boolean isComplete;

    /**
     * Initializes a new instance of the {@link CodeCompletionResult} class.
     *
     * @param proposals the completion proposals, in the order in which
     *                  they are to be presented to the user
     * @param replacementRegion the region to replace with the completion
     * @param isComplete whether the list of completions is complete
     */
    public CodeCompletionResult(ListView<CodeCompletionItem> proposals, Region replacementRegion, @Experimental boolean isComplete) {
        this.proposals = proposals;
        this.replacementRegion = replacementRegion;
        this.isComplete = isComplete;
    }

    /**
     * Gets a list of completion proposals, returned in the order in which
     * they are to be presented to the user.
     *
     * @return a list of completion proposals
     */
    public ListView<CodeCompletionItem> getProposals() {
        return this.proposals;
    }

    /**
     * Gets the region to replace with the code completion.
     *
     * @return the region to replace
     */
    public Region getReplacementRegion() {
        return this.replacementRegion;
    }

    /**
     * Gets whether the list of completions is complete.
     *
     * @return {@code true} when the list is complete;
     * otherwise, {@code false} when narrowing the search (e.g., by typing more characters)
     * may return in new proposals being returned
     */
    @Experimental
    public boolean isComplete() {
        return this.isComplete;
    }

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return internalEquals((CodeCompletionResult)o);
    }

    protected boolean internalEquals(CodeCompletionResult that) {
        return this.proposals.equals(that.proposals)
            && this.replacementRegion.equals(that.replacementRegion)
            && this.isComplete == that.isComplete;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            proposals,
            replacementRegion,
            isComplete
        );
    }

    @Override public String toString() {
        return "CodeCompletionResult{" +
            "proposals=" + proposals + ", " +
            "replacementRegion=" + replacementRegion + ", " +
            "isComplete=" + isComplete +
            '}';
    }
}
