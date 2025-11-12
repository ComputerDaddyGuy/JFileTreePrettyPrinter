package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

/**
 * Utility class providing constants and factory methods for creating
 * {@link ChildLimitBuilder} instances.
 * <p>
 * A child limit defines how many entries (files or directories) are allowed
 * under a given directory path when building or rendering a file tree.
 * </p>
 *
 * <p>
 * This class also exposes the {@link #UNLIMITED} constant, representing
 * an unrestricted number of children.
 * </p>
 *
 * <h2>Example usage:</h2>
 * <pre>{@code
 * var limits = ChildLimits.builder()
 *     .add(PathMatchers.hasName("bin"), 0)      // Hide "bin" folder contents
 *     .add(PathMatchers.hasName("src"), 20)     // Limit to 20 children
 *     .build();
 * }</pre>
 *
 * @see ChildLimitBuilder
 */
public final class ChildLimits {

	/** 
	 * Special value indicating unlimited children ({@code -1}). 
	 */
	public static final int UNLIMITED = -1;

	private ChildLimits() {
		// Helper class
	}

	/**
	 * Returns a new {@link ChildLimitBuilder} with the default limit
	 * set to {@link #UNLIMITED}.
	 *
	 * @return a fresh builder instance
	 */
	public static ChildLimitBuilder builder() {
		return builder(UNLIMITED);
	}

	/**
	 * Returns a new {@link ChildLimitBuilder} with the given default limit.
	 * <p>
	 * The default limit applies when no specific rule matches a given path.
	 * </p>
	 *
	 * @param defaultLimit the default limit (use {@link #UNLIMITED} for no restriction)
	 * @return a fresh builder instance
	 */
	public static ChildLimitBuilder builder(int defaultLimit) {
		return new ChildLimitBuilder(defaultLimit);
	}

}
