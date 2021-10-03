package mb.common.codecompletion;

import mb.common.region.Region;
import mb.common.util.Experimental;
import mb.common.util.ListView;

import java.io.Serializable;

/**
 * The result of invoking completions.
 */
public class CodeCompletionResult implements Serializable {

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
}
