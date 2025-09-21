package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.jspecify.annotations.NullMarked;

/**
 * Utility class providing common {@link Predicate<Path>} implementations
 * and helper methods for testing files and directories.
 * <p>
 * All methods are {@code static} and can be used directly or wrapped into
 * a {@link Predicate<Path>} for filtering paths.
 * </p>
 *
 * This class is not instantiable.
 */
@NullMarked
public final class PathPredicates {

	private PathPredicates() {
		// Helper class
	}

	/**
	 * Creates a new builder, to create advanced predicate.
	 * 
	 * @return a new builder
	 * 
	 * @see PathPredicateBuilder
	 */
	public static PathPredicateBuilder builder() {
		return new PathPredicateBuilder();
	}

	// ---------- Name ----------

	/**
	 * Creates a predicate that tests whether the path has the specified file name
	 *
	 * @param name the expected file name (without parent directories)
	 * 
	 * @return a predicate testing for file names matching the given name
	 */
	public static Predicate<Path> hasName(String name) {
		return path -> PathUtils.hasName(path, name);
	}

	/**
	 * Creates a predicate that tests whether the path has the specified
	 * file name, ignoring case.
	 *
	 * @param name the expected file name (without parent directories), case-insensitive
	 * 
	 * @return a predicate testing for file names matching the given name, case-insensitive
	 */
	public static Predicate<Path> hasNameIgnoreCase(String name) {
		return path -> PathUtils.hasNameIgnoreCase(path, name);
	}

	/**
	 * Creates a predicate that tests whether a path's file name matches
	 * the provided pattern.
	 *
	 * @param pattern the regex pattern to apply to the file name
	 * 
	 * @return a predicate testing for file names matching the pattern
	 */
	public static Predicate<Path> hasNameMatching(Pattern pattern) {
		return path -> PathUtils.hasNameMatching(path, pattern);
	}

	/**
	 * Creates a predicate that tests whether a path's file name ends
	 * with the specified suffix.
	 *
	 * @param suffix the suffix to test (e.g. ".log", ".txt")
	 * 
	 * @return a predicate testing for file names ending with the given suffix
	 */
	public static Predicate<Path> hasNameEndingWith(String suffix) {
		return path -> PathUtils.hasNameEndingWith(path, suffix);
	}

	/**
	 * Creates a predicate that tests whether a path's file name has
	 * the specified extension.
	 * <p>
	 * The extension should be provided without a leading dot, e.g.
	 * {@code "txt"} or {@code "pdf"}.
	 * </p>
	 *
	 * @param extension the extension to test (without the dot)
	 * 
	 * @return a predicate testing for file names with the given extension
	 */
	public static Predicate<Path> hasExtension(String extension) {
		return path -> PathUtils.hasExtension(path, extension);
	}

	// ---------- Type ----------

	/**
	 * Creates a predicate that tests whether the path represents a directory.
	 *
	 * @return a predicate testing for files to represent a directory
	 */
	public static Predicate<Path> isDirectory() {
		return path -> PathUtils.isDirectory(path);
	}

	/**
	 * Creates a predicate that tests whether the path represents a file.
	 *
	 * @return a predicate testing for files to represent a file
	 */
	public static Predicate<Path> isFile() {
		return path -> PathUtils.isFile(path);
	}

}
