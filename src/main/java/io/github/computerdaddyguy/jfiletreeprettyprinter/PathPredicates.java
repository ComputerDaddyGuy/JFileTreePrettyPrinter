package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.nio.file.Path;
import java.util.Objects;
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
		return PathUtils::isDirectory;
	}

	/**
	 * Creates a predicate that tests whether the path represents a file.
	 *
	 * @return a predicate testing for files to represent a file
	 */
	public static Predicate<Path> isFile() {
		return PathUtils::isFile;
	}

	// ---------- Hierarchy ----------

	/**
	 * Creates a predicate that evaluates the direct parent of a given path.
	 * The returned predicate applies the provided {@code parentPredicate} to {@link Path#getParent()} of the tested path.
	 * If tested path has no parent, the returned predicate will always return {@code false}.
	 *
	 * @param parentPredicate the predicate to apply on the parent path (must not be {@code null})
	 * 
	 * @return a predicate that returns {@code true} if the parent of the tested path matches the given predicate
	 * 
	 * @throws NullPointerException if {@code parentPredicate} is {@code null}
	 */
	public static Predicate<Path> hasParentMatching(Predicate<Path> parentPredicate) {
		Objects.requireNonNull(parentPredicate, "parentPredicate is null");
		return path -> PathUtils.hasParentMatching(path, parentPredicate);
	}

	/**
	 * Creates a predicate that evaluates all ancestors of a given path.
	 * <p>
	 * The returned predicate applies the provided {@code ancestorPredicate}
	 * to each parent in the chain obtained via successive calls to {@link Path#getParent()}.
	 * If any ancestor matches, the predicate returns {@code true}.
	 * <p>
	 * If the tested path has no parent, the returned predicate always returns {@code false}.
	 *
	 * @param ancestorPredicate the predicate to apply on each ancestor path (must not be {@code null})
	 * 
	 * @return a predicate that returns {@code true} if any ancestor of the tested path matches the given predicate
	 * 
	 * @throws NullPointerException if {@code ancestorPredicate} is {@code null}
	 */
	public static Predicate<Path> hasAncestorMatching(Predicate<Path> ancestorPredicate) {
		Objects.requireNonNull(ancestorPredicate, "ancestorPredicate is null");
		return path -> PathUtils.hasAncestorMatching(path, ancestorPredicate);
	}

	/**
	 * Creates a predicate that evaluates the direct children of a given path.
	 * <p>
	 * The returned predicate applies the provided {@code childPredicate} to each
	 * direct child of the tested path. The predicate returns {@code true} if at least
	 * one child matches. If the path is not a directory, the predicate always returns {@code false}.
	 *
	 * @param childPredicate the predicate to apply on each direct child
	 * 
	 * @return a predicate that returns {@code true} if at least one direct child matches
	 * 
	 * @throws NullPointerException if {@code childPredicate} is {@code null}
	 */
	public static Predicate<Path> hasDirectChildMatching(Predicate<Path> childPredicate) {
		Objects.requireNonNull(childPredicate, "childPredicate is null");
		return path -> PathUtils.hasDirectChildMatching(path, childPredicate);
	}

	/**
	 * Creates a predicate that evaluates all descendants (children at any depth) of a given path.
	 * <p>
	 * The returned predicate applies the provided {@code descendantPredicate} recursively to each
	 * child and sub-child. It returns {@code true} if at least one descendant matches.
	 *
	 * @param descendantPredicate the predicate to apply on each descendant
	 * 
	 * @return a predicate that returns {@code true} if at least one descendant matches
	 * 
	 * @throws NullPointerException if {@code descendantPredicate} is {@code null}
	 */
	public static Predicate<Path> hasDescendantMatching(Predicate<Path> descendantPredicate) {
		Objects.requireNonNull(descendantPredicate, "descendantPredicate is null");
		return path -> PathUtils.hasDescendantMatching(path, descendantPredicate);
	}

	/**
	 * Creates a predicate that evaluates the siblings of a given path.
	 * <p>
	 * The returned predicate applies the provided {@code siblingPredicate} to each
	 * sibling of the tested path (other files/directories in the same parent).
	 * The predicate returns {@code true} if at least one sibling matches.
	 * If the tested path has no parent, the predicate always returns {@code false}.
	 *
	 * @param siblingPredicate the predicate to apply on each sibling
	 * 
	 * @return a predicate that returns {@code true} if at least one sibling matches
	 * 
	 * @throws NullPointerException if {@code siblingPredicate} is {@code null}
	 */
	public static Predicate<Path> hasSiblingMatching(Predicate<Path> siblingPredicate) {
		Objects.requireNonNull(siblingPredicate, "siblingPredicate is null");
		return path -> PathUtils.hasSiblingMatching(path, siblingPredicate);
	}

}
