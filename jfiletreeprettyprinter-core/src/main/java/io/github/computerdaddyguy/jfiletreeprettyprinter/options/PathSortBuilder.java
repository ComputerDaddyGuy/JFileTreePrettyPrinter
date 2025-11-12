package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import org.jspecify.annotations.NullMarked;

/**
 * A builder for creating a {@link Comparator Comparator&lt;Path&gt;} that defines
 * a custom sorting order for file system paths based on rule precedence.
 * <p>
 * Each rule assigns an integer "precedence" value to paths. The first rule
 * that matches a path determines its precedence. Paths are then sorted by
 * ascending precedence value (lower values come first), followed by an
 * alphabetical fallback comparison.
 * </p>
 *
 * <p>
 * Predefined precedence constants:
 * </p>
 * <ul>
 *   <li>{@link #HIGHEST_PRECEDENCE} ({@code Integer.MIN_VALUE}) — top priority</li>
 *   <li>{@link #DEFAULT_PRECEDENCE} ({@code 0}) — default order</li>
 *   <li>{@link #LOWEST_PRECEDENCE} ({@code Integer.MAX_VALUE}) — last priority</li>
 * </ul>
 *
 * <h2>Example usage:</h2>
 * <pre>{@code
 * var customSort = PathSortBuilder.newInstance()
 *     .addFirst(PathMatchers.hasName("README.md"))     // always first
 *     .addLast(PathMatchers.hasName("target"))         // always last
 *     .add(path -> path.toString().contains("core") ? -10 : 0) // custom priority rule
 *     .build();
 *
 * var printer = FileTreePrettyPrinter.builder()
 *     .customizeOptions(options -> options.sort(customSort))
 *     .build();
 * }</pre>
 */
@NullMarked
public class PathSortBuilder {

	/** Highest possible precedence — items appear first. */
	public static final int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/** Default precedence (neutral value). */
	public static final int DEFAULT_PRECEDENCE = 0;

	/** Lowest possible precedence — items appear last. */
	public static final int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

	private List<ToIntFunction<Path>> orders;

	/* package */ PathSortBuilder() {
		this.orders = new ArrayList<>();
	}

	/**
	 * Builds the final {@link Comparator Comparator&lt;Path&gt;} based on the configured rules.
	 * <p>
	 * Rules are tested in the order they were added. The first matching rule
	 * having a result different than {@link DEFAULT_PRECEDENCE} (meaning, {@code 0})
	 * determines the precedence value for a given path. Paths are sorted by
	 * this precedence, and then alphabetically as a tiebreaker.
	 * </p>
	 *
	 * @return a comparator defining the final path order
	 */
	public Comparator<Path> build() {
		var immutOrders = List.copyOf(orders);
		Function<Path, Integer> finalFunction = path -> {
			int result = DEFAULT_PRECEDENCE;
			for (var rule : immutOrders) {
				result = rule.applyAsInt(path);
				if (result != DEFAULT_PRECEDENCE) {
					break;
				}
			}
			return result;
		};
		return Comparator.comparing(finalFunction).thenComparing(PathSorts.ALPHABETICAL);
	}

	/**
	 * Adds a custom rule function defining a precedence for a path.
	 *
	 * @param order a function returning a precedence value
	 * @return this builder for chaining
	 *
	 * @throws NullPointerException if {@code order} is null
	 */
	public PathSortBuilder add(ToIntFunction<Path> order) {
		Objects.requireNonNull(order, "order is null");
		this.orders.add(order);
		return this;
	}

	/**
	 * Adds a rule that assigns a precedence value to all paths matching
	 * the specified {@link PathMatcher}.
	 *
	 * @param pathMatcher the matcher to test paths
	 * @param order the precedence value to assign
	 * @return this builder for chaining
	 *
	 * @throws NullPointerException if {@code pathMatcher} is null
	 */
	public PathSortBuilder add(PathMatcher pathMatcher, int order) {
		Objects.requireNonNull(pathMatcher, "pathMatcher is null");
		return add(path -> pathMatcher.matches(path) ? order : DEFAULT_PRECEDENCE);
	}

	/**
	 * Adds a rule that forces matching paths to appear first in the sort order.
	 *
	 * @param pathMatcher the matcher identifying high-priority paths
	 * @return this builder for chaining
	 */
	public PathSortBuilder addFirst(PathMatcher pathMatcher) {
		return add(pathMatcher, HIGHEST_PRECEDENCE);
	}

	/**
	 * Adds a rule that forces matching paths to appear last in the sort order.
	 *
	 * @param pathMatcher the matcher identifying low-priority paths
	 * @return this builder for chaining
	 */
	public PathSortBuilder addLast(PathMatcher pathMatcher) {
		return add(pathMatcher, LOWEST_PRECEDENCE);
	}

}
