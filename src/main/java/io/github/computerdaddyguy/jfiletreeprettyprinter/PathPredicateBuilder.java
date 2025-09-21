package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A builder for composing complex {@link Predicate Predicates} on {@link Path} objects.
 * <p>
 * Predicates are combined using logical {@code and}, allowing you to chain multiple conditions.
 * 
 * <h2>Example usage:</h2>
 * <pre>{@code
 * var predicate = PathPredicates.builder()
 *     .isDirectory()
 *     .hasNameIgnoreCase("src")
 *     .build();
 *
 * // Tests true only for directories named "src" (case-insensitive).
 * boolean matches = predicate.test(Path.of("src"));
 * }</pre>
 */
@NullMarked
public class PathPredicateBuilder {

	private Predicate<Path> combinedPredicate;

	/**
	 * Creates a new builder.
	 */
	public PathPredicateBuilder() {
		combinedPredicate = null;
	}

	/**
	 * Builds the composed predicate.
	 * 
	 * @return the final combined predicate, <code>null</code> if no predicate set
	 */
	@Nullable
	public Predicate<Path> build() {
		return combinedPredicate;
	}

	// ---------- General predicates ----------

	/**
	 * Adds the given path-based predicate to the chain.
	 * 
	 * @param predicate	predicate to add
	 * 
	 * @return this builder for chaining
	 */
	public PathPredicateBuilder pathTest(Predicate<Path> predicate) {
		Objects.requireNonNull(predicate, "predicate is null");
		combinedPredicate = combinedPredicate == null ? predicate : combinedPredicate.and(predicate);
		return this;
	}

	/**
	 * Adds the given file-based predicate to the chain.
	 * <p>
	 * The {@link Path} is converted to a {@link File} using {@code path.toFile()}.
	 * 
	 * @param predicate	predicate to add
	 * 
	 * @return this builder for chaining
	 */
	public PathPredicateBuilder fileTest(Predicate<File> predicate) {
		return pathTest(path -> predicate.test(path.toFile()));
	}

	// ---------- Name ----------

	/**
	 * Adds a condition that tests whether the path has exactly
	 * the specified file name.
	 * 
	 * @param name the expected file name (without parent directories)
	 * 
	 * @return this builder for chaining
	 */
	public PathPredicateBuilder hasName(String name) {
		return pathTest(PathPredicates.hasName(name));
	}

	/**
	 * Adds a condition that tests whether the path has the specified
	 * file name, ignoring case.
	 *
	 * @param name the expected file name (without parent directories), case-insensitive
	 * 
	 * @return this builder for chaining
	 */
	public PathPredicateBuilder hasNameIgnoreCase(String name) {
		return pathTest(PathPredicates.hasNameIgnoreCase(name));
	}

	/**
	 * Adds a condition that tests whether the given path's file name matches the provided pattern.
	 *
	 * @param pattern the regex pattern to apply to the file name
	 * 
	 * @return this builder for chaining
	 */
	public PathPredicateBuilder hasNameMatching(Pattern pattern) {
		return pathTest(PathPredicates.hasNameMatching(pattern));
	}

	/**
	 * Adds a condition that tests whether the given path's file name ends with the specified suffix.
	 *
	 * @param suffix the suffix to test (e.g. ".log", ".txt")
	 * 
	 * @return this builder for chaining
	 */
	public PathPredicateBuilder hasNameEndingWith(String suffix) {
		return pathTest(PathPredicates.hasNameEndingWith(suffix));
	}

	/**
	 * Adds a condition that tests whether the given path's file name has the specified extension.
	 * <p>
	 * The extension should be provided without a leading dot, e.g.
	 * {@code "txt"} or {@code "pdf"}.
	 * </p>
	 *
	 * @param extension the extension to test (without the dot)
	 * 
	 * @return this builder for chaining
	 */
	public PathPredicateBuilder hasExtension(String extension) {
		return pathTest(PathPredicates.hasExtension(extension));
	}

	// ---------- Type ----------

	/**
	 * Adds a condition that tests whether the path represents a directory.
	 *
	 * @return this builder for chaining
	 */
	public PathPredicateBuilder isDirectory() {
		return pathTest(PathPredicates.isDirectory());
	}

	/**
	 * Adds a condition that tests whether the path represents a file.
	 *
	 * @return this builder for chaining
	 */
	public PathPredicateBuilder isFile() {
		return pathTest(PathPredicates.isFile());
	}

}