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
 * The resulting function evaluates added rules in the order they were defined.
 * The first matching rule determines the child limit for a given path.
 * If no rules match, the default limit is applied.
 * </p>
 *
 * <p>
 * A limit of {@link #UNLIMITED} ({@code -1}) means no restriction.
 * A limit of {@code 0} means the directory cannot have any children.
 * </p>
 *
 * <h2>Example usage:</h2>
 * <pre>{@code
 * var childLimit = ChildLimitBuilder.builder()
 *     .defaultLimit(ChildLimit.UNLIMITED)   // unlimited unless specified
 *     .limit(PathMatchers.hasName("bigDir"), 10)  // max 10 children in "bigDir"
 *     .limit(PathMatchers.hasName("emptyDir"), 0) // disallow children in "emptyDir"
 *     .build();
 *
 * }</pre>
 */
@NullMarked
public class ChildLimitBuilder {

	/**
	 * Unlimited children.
	 */
	public static final int UNLIMITED = -1;

	private static final ChildControl UNLIMITED_CONTROL = new ChildControl(p -> true, UNLIMITED);

	private List<ChildControl> controls;
	private ChildControl defaultControl;

	/**
	 * Creates a new builder.
	 */
	private ChildLimitBuilder() {
		this.controls = new ArrayList<>();
		this.defaultControl = UNLIMITED_CONTROL;
	}

	private record ChildControl(PathMatcher pathMatcher, int limit) {

	}

	public static ChildLimitBuilder builder() {
		return new ChildLimitBuilder();
	}

	/**
	 * Builds the child limit function based on the configured rules.
	 * <p>
	 * Rules are tested in the order they were added. The first matching rule
	 * provides the limit. If no rule matches, the default limit is used.
	 * </p>
	 *
	 * @return a function mapping a {@link Path} to its maximum number of children
	 */
	public ToIntFunction<Path> build() {
		var immutControls = List.copyOf(controls);
		var immutDefaultControl = this.defaultControl;
		return p -> immutControls.stream()
			.filter(control -> control.pathMatcher().matches(p))
			.findFirst()
			.orElse(immutDefaultControl)
			.limit();
	}

	/**
	 * Sets the default child limit to apply when no specific rule matches.
	 *
	 * @param limit the default limit (use {@link #UNLIMITED} for no restriction)
	 * 
	 * @return this builder for chaining
	 */
	public ChildLimitBuilder defaultLimit(int limit) {
		this.defaultControl = new ChildControl(p -> true, limit);
		return this;
	}

	/**
	 * Adds a child limit rule for paths matching the given matcher.
	 * <p>
	 * Rules are evaluated in the order they are added. The first matching rule wins.
	 * </p>
	 *
	 * @param pathMatcher the condition for paths
	 * @param limit the maximum number of children (use {@link #UNLIMITED} for no restriction)
	 * 
	 * @return this builder for chaining
	 * 
	 * @throws NullPointerException if {@code pathMatcher} is null
	 */
	public ChildLimitBuilder limit(PathMatcher pathMatcher, int limit) {
		Objects.requireNonNull(pathMatcher, "pathMatcher is null");
		this.controls.add(new ChildControl(pathMatcher, limit));
		return this;
	}

}
