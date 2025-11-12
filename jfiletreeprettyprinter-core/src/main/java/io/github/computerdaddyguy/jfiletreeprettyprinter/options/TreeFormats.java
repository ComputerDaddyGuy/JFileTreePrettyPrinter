package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

public final class TreeFormats {

	/**
	 * Uses characters: |--, `-- and │
	 */
	public static final TreeFormat CLASSIC_ASCII = new TreeFormat("|--", "`--", "│  ", "   ");

	/**
	 * Uses characters: ├─, └─ and │
	 */
	public static final TreeFormat UNICODE_BOX_DRAWING = new TreeFormat("├─", "└─", "│ ", "  ");

	private TreeFormats() {
		// Helper class
	}

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
	 * 
	 * @see TreeFormat
	 */
	public static TreeFormat create(String nonLastChildSymbol, String lastChildSymbol, String continuationSymbol, String emptySymbol) {
		return new TreeFormat(nonLastChildSymbol, lastChildSymbol, continuationSymbol, emptySymbol);
	}

}
