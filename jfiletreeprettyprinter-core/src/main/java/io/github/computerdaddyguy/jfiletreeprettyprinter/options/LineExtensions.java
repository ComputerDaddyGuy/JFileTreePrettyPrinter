package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

/**
 * Utility class providing constants and factory methods for creating
 * {@link LineExtensionBuilder} instances used to define per-path line extensions
 * in pretty-printed file trees.
 * <p>
 * Line extensions are optional text fragments appended to specific lines
 * in the printed tree. They can be used to add comments, annotations, or
 * formatting cues.
 * </p>
 *
 * <p>
 * Two special constants are provided:
 * </p>
 * <ul>
 *   <li>{@link #NO_EXTENSION} — indicates no extension for a given path.</li>
 *   <li>{@link #LINE_BREAK_EXTENSION} — indicates a forced line break,
 *       useful for splitting compact directory chains.</li>
 * </ul>
 *
 * <h2>Example usage:</h2>
 * <pre>{@code
 * var lineExt = LineExtensions.builder()
 *     .add(PathMatchers.hasName("README.md"), "\t// Project documentation")
 *     .addLineBreak(PathMatchers.hasRelativePathMatchingGlob(".", "src/main/java"))
 *     .build();
 * }</pre>
 *
 * @see LineExtensionBuilder
 */
public final class LineExtensions {

	/** Indicates that no extension should be applied to the line ({@code null}). */
	public static final String NO_EXTENSION = null;

	/** Indicates a forced line break (empty string). */
	public static final String LINE_BREAK_EXTENSION = "";

	private LineExtensions() {
		// Helper class
	}

	/**
	 * Returns a new {@link LineExtensionBuilder}.
	 *
	 * @return a fresh builder instance
	 */
	public static LineExtensionBuilder builder() {
		return new LineExtensionBuilder();
	}

}
