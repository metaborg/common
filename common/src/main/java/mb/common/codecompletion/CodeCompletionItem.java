package mb.common.codecompletion;

import mb.common.editing.TextEdit;
import mb.common.style.StyleName;
import mb.common.util.Experimental;
import mb.common.util.ListView;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A completion proposal.
 */
public class CodeCompletionItem implements Serializable {

    private final String label;
    private final String description;
    private final String parameters;
    private final String type;
    private final String location;
    private final StyleName kind;
    private final boolean deprecated;
    private final ListView<TextEdit> edits;

    /**
     * Initializes a new instance of the {@link CodeCompletionItem} class.
     *
     * @param label       the label of the proposal
     * @param description the details of the proposal; or an empty string
     * @param parameters  the parameters of the proposal; or an empty string
     * @param type        the type of the proposal; or an empty string
     * @param location    the location of the proposal; or an empty string
     * @param kind        the kind of proposal
     * @param edits       the edits to perform to insert the proposal
     * @param deprecated  whether the proposal is deprecated
     */
    public CodeCompletionItem(String label, String description, String parameters, String type, String location, StyleName kind, ListView<TextEdit> edits, @Experimental boolean deprecated) {
        this.label = label;
        this.description = description;
        this.parameters = parameters;
        this.type = type;
        this.location = location;
        this.kind = kind;
        this.deprecated = deprecated;
        this.edits = edits;
    }

    /**
     * Gets the label to be displayed to the user.
     *
     * @return the label to display
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the description of the proposal,
     * such as a short description of a template,
     * the method being overridden,
     * or the field for which it is a getter.
     *
     * @return the description; or an empty string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the parameters of the proposal, such as type parameters and method parameters
     * including brackets and parentheses.
     *
     * @return the parameter string; or an empty string
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * Gets the type of the proposal, such as the field type or return type
     *
     * @return the type string; or an empty string
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the location string of the proposal, such as the namespace, package, or class.
     *
     * @return the location string; or an empty string
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the kind of proposal.
     *
     * This is used to render the proposal in an IDE-specific way.
     *
     * @return the style name of the kind of proposal
     */
    public StyleName getKind() {
        return kind;
    }

    /**
     * Gets a list of text edits to perform when this proposal is inserted.
     *
     * Most proposals will only insert text at the caret location,
     * but some proposals might additionally require text to be inserted at
     * other locations in the document, such as adding an import statement
     * or a qualifier.
     *
     * @return a list of text edits, where the first edit is the primary one
     */
    public ListView<TextEdit> getEdits() {
        return edits;
    }

    /**
     * Gets whether the proposal proposes something that is deprecated.
     *
     * @return {@code true} when the proposed is deprecated;
     * otherwise, {@code false}.
     */
    @Experimental
    public boolean isDeprecated() {
        return deprecated;
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return innerEquals((CodeCompletionItem)o);
    }

    /**
     * Determines whether this object is equal to the specified object.
     *
     * Note: this method does not check whether the type of the argument is exactly the same.
     *
     * @param that the object to compare to
     * @return {@code true} when this object is equal to the specified object;
     * otherwise, {@code false}
     */
    protected boolean innerEquals(CodeCompletionItem that) {
        return this.deprecated == that.deprecated
            && this.label.equals(that.label)
            && this.description.equals(that.description)
            && this.parameters.equals(that.parameters)
            && this.type.equals(that.type)
            && this.location.equals(that.location)
            && this.kind.equals(that.kind)
            && this.edits.equals(that.edits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            label,
            description,
            parameters,
            type,
            location,
            kind,
            deprecated,
            edits
        );
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(label);
        sb.append(parameters);
        if (!location.isEmpty()) sb.append(" @ ").append(location);
        if (!type.isEmpty()) sb.append(" : ").append(type);
        if (!description.isEmpty()) sb.append(" (").append(description).append(")");
        return sb.toString();
    }
}
