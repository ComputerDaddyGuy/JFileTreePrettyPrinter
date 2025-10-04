package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;
import org.jspecify.annotations.NullMarked;

/**
 * A builder for creating a {@code ToIntFunction<Path>} that defines
 * how many child entries (files or directories) are allowed under a given path.
 * <p>
 * Rules are evaluated in the order they are added (first-match-wins).
 * The first rule that matches a path determines the limit.
 * If no rules match, the default limit is applied.
 * </p>
 *
 * <p>
 * Special values:
 * <ul>
 *   <li>{@link #UNLIMITED} ({@code -1}) — no restriction on children.</li>
 *   <li>{@code 0} — directory cannot contain any children.</li>
 *   <li>Any positive number — maximum number of children allowed.</li>
 * </ul>
 * </p>
 *
 * <h2>Example usage:</h2>
 * <pre>{@code
 * var childLimit = ChildLimitBuilder.newInstance()
 *     .setDefault(ChildLimitBuilder.UNLIMITED)     // unlimited unless specified
 *     .add(PathMatchers.hasName("bigDir"), 10)     // max 10 children in "bigDir"
 *     .add(PathMatchers.hasName("emptyDir"), 0)    // disallow children in "emptyDir"
 *     .build();
 * }</pre>
 */
@NullMarked
public class ChildLimitBuilder {

	/** 
	 * Special value indicating unlimited children ({@code -1}). 
	 */
	public static final int UNLIMITED = -1;

	private List<ToIntFunction<Path>> limits;
	private int defaultLimit;

	private ChildLimitBuilder(int defaultLimit) {
		this.limits = new ArrayList<>();
		this.defaultLimit = defaultLimit;
	}

	/**
	 * Returns a new {@link ChildLimitBuilder}.
	 *
	 * @return a fresh builder instance
	 */
	public static ChildLimitBuilder newInstance() {
		return newInstance(UNLIMITED);
	}

	public static ChildLimitBuilder newInstance(int defaultLimit) {
		return new ChildLimitBuilder(defaultLimit);
	}

	/**
	 * Builds the final child limit function based on the configured rules.
	 * <p>
	 * Rules are evaluated in insertion order. The first rule that matches a path
	 * determines its child limit. If no rules match, the default limit is returned.
	 * </p>
	 *
	 * @return a function mapping a {@link Path} to its maximum number of children
	 */
	public ToIntFunction<Path> build() {
		var immutLimits = List.copyOf(limits);
		return path -> {
			int result = defaultLimit;
			for (var rule : immutLimits) {
				result = rule.applyAsInt(path);
				if (result >= 0) {
					break;
				}
			}
			return result;
		};
	}

	/**
	 * Sets the default child limit to apply when no specific rule matches.
	 *
	 * @param limit the default limit (use {@link #UNLIMITED} for no restriction)
	 * @return this builder for chaining
	 */
	public ChildLimitBuilder setDefault(int limit) {
		this.defaultLimit = limit;
		return this;
	}

	/**
	 * Adds a custom rule expressed as a {@link ToIntFunction}.
	 * <p>
	 * This function should return:
	 * <ul>
	 *   <li>{@link #UNLIMITED} ({@code -1}) if it does not apply</li>
	 *   <li>Any non-negative integer as the effective child limit if it applies</li>
	 * </ul>
	 * </p>
	 *
	 * @param childLimit the function mapping a path to a child limit
	 * @return this builder for chaining
	 * @throws NullPointerException if {@code childLimit} is null
	 */
	public ChildLimitBuilder add(ToIntFunction<Path> childLimit) {
		Objects.requireNonNull(childLimit, "childLimit is null");
		this.limits.add(childLimit);
		return this;
	}

	/**
	 * Adds a child limit rule for paths matching the given matcher.
	 * <p>
	 * If the path matches, this rule applies the given limit.
	 * Otherwise, it yields {@link #UNLIMITED} so that subsequent rules may apply.
	 * Because rules are evaluated in insertion order, the first matching rule wins.
	 * </p>
	 *
	 * @param pathMatcher the matcher used to test paths (non-null)
	 * @param limit the maximum number of children for matching paths
	 *              (use {@link #UNLIMITED} for no restriction)
	 * @return this builder for chaining
	 * @throws NullPointerException if {@code pathMatcher} is null
	 */
	public ChildLimitBuilder add(PathMatcher pathMatcher, int limit) {
		Objects.requireNonNull(pathMatcher, "pathMatcher is null");
		return add(path -> pathMatcher.matches(path) ? limit : UNLIMITED);
	}

}
