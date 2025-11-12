package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

import java.util.List;

/**
 * Represents the ASCII or Unicode symbol set used to render the visual
 * structure of a directory tree.
 *
 * <p>A {@code TreeFormat} defines the characters or strings used for:
 * <ul>
 *   <li>the branch connecting non-last children,</li>
 *   <li>the branch connecting the last child,</li>
 *   <li>the vertical continuation line for deeper levels, and</li>
 *   <li>the placeholder for empty indentation (no connection line).</li>
 * </ul>
 * </p>
 *
 * <p>All four symbols must have the <strong>same display width</strong>
 * (i.e., same string length) to ensure proper alignment of the printed tree.
 * A mismatch will cause an {@link IllegalArgumentException} at construction time.</p>
 *
 * <h2>Example:</h2>
 * <pre>{@code
 * var treeFormat = new TreeFormat("├─", "└─", "│ ", "  ");
 * }</pre>
 *
 * @see TreeFormats
 */
public class TreeFormat {

	private final String nonLastChildSymbol;
	private final String lastChildSymbol;
	private final String continuationSymbol;
	private final String emptySymbol;

	/**
	 * Constructs a new {@link TreeFormat} instance.
	 *
	 * @param nonLastChildSymbol the symbol used to connect non-last children (e.g. {@code "├─"})
	 * @param lastChildSymbol the symbol used to connect the last child (e.g. {@code "└─"})
	 * @param continuationSymbol the symbol used for vertical continuation (e.g. {@code "│ "})
	 * @param emptySymbol the symbol used for empty indentation (e.g. {@code "  "})
	 * 
	 * @throws NullPointerException if any argument is {@code null}
	 * @throws IllegalArgumentException if the provided symbols do not all have the same length
	 */
	public TreeFormat(String nonLastChildSymbol, String lastChildSymbol, String continuationSymbol, String emptySymbol) {
		super();
		ensureAllSameLength(nonLastChildSymbol, lastChildSymbol, continuationSymbol, emptySymbol);
		this.nonLastChildSymbol = nonLastChildSymbol;
		this.lastChildSymbol = lastChildSymbol;
		this.continuationSymbol = continuationSymbol;
		this.emptySymbol = emptySymbol;
	}

	private void ensureAllSameLength(String nonLastChildSymbol, String lastChildSymbol, String continuationSymbol, String emptySymbol) {
		var sizeCountList = List.of(nonLastChildSymbol, lastChildSymbol, continuationSymbol, emptySymbol)
			.stream()
			.map(String::length)
			.distinct()
			.toList();
		if (sizeCountList.size() != 1) {
			throw new IllegalArgumentException("All tree symbols must have the same length");
		}
	}

	/**
	 * Returns the symbol used for non-last child connections.
	 *
	 * @return the non-last child symbol
	 */
	public String getNonLastChildSymbol() {
		return nonLastChildSymbol;
	}

	/**
	 * Returns the symbol used for last child connections.
	 *
	 * @return the last child symbol
	 */
	public String getLastChildSymbol() {
		return lastChildSymbol;
	}

	/**
	 * Returns the symbol used for continuing lines in deeper levels.
	 *
	 * @return the continuation symbol
	 */
	public String getContinuationSymbol() {
		return continuationSymbol;
	}

	/**
	 * Returns the symbol used for empty indentation (no connection line).
	 *
	 * @return the empty indentation symbol
	 */
	public String getEmptySymbol() {
		return emptySymbol;
	}

}
