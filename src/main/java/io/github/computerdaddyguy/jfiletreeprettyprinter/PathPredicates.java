package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;
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

	// ---------- PathMatcher ----------

	/**
	 * Tests whether the given {@link Path} matches the specified glob pattern.
	 *
	 * <p>The glob syntax follows {@link java.nio.file.FileSystem#getPathMatcher(String)} conventions.
	 *
	 * @param path the path to test; must not be {@code null}
	 * @param glob the glob pattern; must not be {@code null}
	 * 
	 * @return {@code true} if the path matches the glob pattern, {@code false} otherwise
	 * 
	 * @throws NullPointerException if {@code path} or {@code glob} is {@code null}
	 * 
	 * @see #hasNameMatchingGlob(Path, String)
	 * @see #hasFullPathMatching(Path, PathMatcher)
	 */
	public static boolean hasFullPathMatchingGlob(Path path, String glob) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(glob, "glob is null");

		// From Files.newDirectoryStream(Path dir, String glob)
		if (glob.equals("*")) {
			return true;
		}
		var matcher = path.getFileSystem().getPathMatcher("glob:" + glob);
		return matcher.matches(path);
	}

	/**
	 * Checks if the given {@link Path} matches the provided {@link PathMatcher}.
	 *
	 * @param path    the path to test; must not be {@code null}
	 * @param matcher the {@code PathMatcher} to use; must not be {@code null}
	 * 
	 * @return {@code true} if the path matches the matcher, {@code false} otherwise
	 * 
	 * @throws NullPointerException if {@code path} or {@code matcher} is {@code null}
	 * 
	 * @see #hasFullPathMatchingGlob(Path, String)
	 */
	public static boolean hasFullPathMatching(Path path, PathMatcher matcher) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(matcher, "matcher is null");
		return matcher.matches(path);
	}

	// ---------- Name ----------

	/**
	 * Tests whether the given path has exactly the specified file name.
	 * 
	 * @param path the path to test
	 * @param name the expected file name (without parent directories)
	 * 
	 * @return {@code true} if the path's file name equals {@code name}
	 * 
	 * @throws NullPointerException if {@code path} or {@code name} is {@code null}
	 */
	public static boolean hasName(Path path, String name) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(name, "name is null");
		return path.getFileName().toString().equals(name);
	}

	/**
	 * Tests whether the given path has the specified file name,
	 * ignoring case.
	 *
	 * @param path the path to test
	 * @param name the expected file name (case-insensitive)
	 * 
	 * @return {@code true} if the path's file name equals {@code name}, ignoring case
	 * 
	 * @throws NullPointerException if {@code path} or {@code name} is {@code null}
	 */
	public static boolean hasNameIgnoreCase(Path path, String name) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(name, "name is null");
		return path.getFileName().toString().equalsIgnoreCase(name);
	}

	/**
	 * Tests whether the given path's file name matches the provided pattern.
	 *
	 * @param path the path to test
	 * @param pattern the regex pattern to apply to the file name
	 * 
	 * @return {@code true} if the file name matches the pattern
	 * 
	 * @throws NullPointerException if {@code path} or {@code pattern} is {@code null}
	 */
	public static boolean hasNameMatching(Path path, Pattern pattern) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(pattern, "pattern is null");
		return pattern.matcher(path.getFileName().toString()).matches();
	}

	/**
	 * Tests whether the given path's file name matches the provided glob.
	 *
	 * <p>The glob syntax follows {@link java.nio.file.FileSystem#getPathMatcher(String)} conventions.
	 * 
	 * @param path the path to test
	 * @param glob the glob pattern to match against the file name; must not be {@code null}
	 * 
	 * @return {@code true} if the file name matches the glob
	 * 
	 * @throws NullPointerException if {@code path} or {@code glob} is {@code null}
	 * 
	 * @see #hasFullPathMatchingGlob(Path, String)
	 */
	public static boolean hasNameMatchingGlob(Path path, String glob) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(glob, "glob is null");
		return hasFullPathMatchingGlob(path.getFileName(), glob);
	}

	/**
	 * Tests whether the given path's file name starts with the specified suffix.
	 *
	 * @param path the path to test
	 * @param prefix the prefix to test (e.g. ".", "analysis-")
	 * 
	 * @return {@code true} if the file name starts with the given prefix
	 * 
	 * @throws NullPointerException if {@code path} or {@code prefix} is {@code null}
	 */
	public static boolean hasNameStartingWith(Path path, String prefix) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(prefix, "prefix is null");
		return path.getFileName().toString().startsWith(prefix);
	}

	/**
	 * Tests whether the given path's file name ends with the specified suffix.
	 *
	 * @param path the path to test
	 * @param suffix the suffix to test (e.g. ".log", ".txt")
	 * 
	 * @return {@code true} if the file name ends with the given suffix
	 * 
	 * @throws NullPointerException if {@code path} or {@code suffix} is {@code null}
	 */
	public static boolean hasNameEndingWith(Path path, String suffix) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(suffix, "suffix is null");
		return path.getFileName().toString().endsWith(suffix);
	}

	/**
	 * Tests whether the given path's file name has the specified extension.
	 * <p>
	 * The extension should be provided without a leading dot, e.g.
	 * {@code "txt"} or {@code "pdf"}.
	 * </p>
	 *
	 * @param path the path to test
	 * @param extension the extension to test (without the dot)
	 * 
	 * @return {@code true} if the file name ends with {@code "." + extension}
	 * 
	 * @throws NullPointerException if {@code path} or {@code extension} is {@code null}
	 */
	public static boolean hasExtension(Path path, String extension) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(extension, "extension is null");
		return hasNameEndingWith(path, "." + extension);
	}

	// ---------- Type ----------

	/**
	 * Tests whether the given path represents a directory.
	 *
	 * @param path the path to test
	 * 
	 * @return {@code true} if the path is a directory
	 * 
	 * @throws NullPointerException if {@code path} is {@code null}
	 */
	public static boolean isDirectory(Path path) {
		Objects.requireNonNull(path, "path is null");
		return path.toFile().isDirectory();
	}

	/**
	 * Tests whether the given path represents a file.
	 *
	 * @param path the path to test
	 * 
	 * @return {@code true} if the path is a file
	 * 
	 * @throws NullPointerException if {@code path} is {@code null}
	 */
	public static boolean isFile(Path path) {
		Objects.requireNonNull(path, "path is null");
		return path.toFile().isFile();
	}

	// ---------- Hierarchy ----------

	/**
	 * Tests whether the direct parent of the given path matches the provided predicate.
	 *
	 * @param path the path whose parent should be tested (must not be {@code null})
	 * @param parentPredicate the predicate to apply to the direct parent (must not be {@code null})
	 *
	 * @return {@code true} if the path has a parent and that parent matches the predicate,
	 *         {@code false} otherwise
	 *
	 * @throws NullPointerException if {@code path} or {@code parentPredicate} is {@code null}
	 */
	public static boolean hasParentMatching(Path path, Predicate<Path> parentPredicate) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(parentPredicate, "parentPredicate is null");

		if (path.getParent() == null) {
			return false;
		}
		return parentPredicate.test(path.getParent());
	}

	/**
	 * Tests whether any ancestor of the given path matches the provided predicate.
	 *
	 * <p>The test is applied recursively up the parent chain (using {@link Path#getParent()})
	 * until the root is reached or the predicate returns {@code true}.
	 *
	 * @param path the path whose ancestors should be tested (must not be {@code null})
	 * @param ancestorPredicate the predicate to apply to each ancestor (must not be {@code null})
	 *
	 * @return {@code true} if any ancestor of the path matches the predicate,
	 *         {@code false} otherwise
	 *
	 * @throws NullPointerException if {@code path} or {@code ancestorPredicate} is {@code null}
	 */
	public static boolean hasAncestorMatching(Path path, Predicate<Path> ancestorPredicate) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(ancestorPredicate, "ancestorPredicate is null");

		Path parent = path.getParent();
		while (parent != null) {
			if (ancestorPredicate.test(parent)) {
				return true;
			}
			parent = parent.getParent();
		}
		return false;
	}

	/**
	 * Tests whether the given path has at least one direct child that matches the provided predicate.
	 *
	 * <p>The method checks only immediate children of the path (not recursive).
	 * If the path is not a directory, the result is always {@code false}.
	 *
	 * @param path the directory whose direct children should be tested (must not be {@code null})
	 * @param childPredicate the predicate to apply to each direct child (must not be {@code null})
	 *
	 * @return {@code true} if any direct child matches the predicate,
	 *         {@code false} if none match, or if the path is not a directory
	 *
	 * @throws NullPointerException if {@code path} or {@code childPredicate} is {@code null}
	 */
	public static boolean hasDirectChildMatching(Path path, Predicate<Path> childPredicate) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(childPredicate, "childPredicate is null");

		File file = path.toFile();
		if (!file.isDirectory()) {
			return false;
		}
		File[] children = file.listFiles();
		if (children == null) { // Only if I/O error occurred when listing files
			return false;
		}
		for (File child : children) {
			if (childPredicate.test(child.toPath())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests whether the given path has at least one descendant (child, grandchild, etc.)
	 * that matches the provided predicate.
	 *
	 * <p>The method walks the file tree starting at the given path, excluding the path itself,
	 * and applies the predicate to all discovered descendants.
	 * If the path is not a directory, the result is always {@code false}.
	 *
	 * @param path the directory whose descendants should be tested (must not be {@code null})
	 * @param descendantPredicate the predicate to apply to each descendant (must not be {@code null})
	 *
	 * @return {@code true} if any descendant matches the predicate,
	 *         {@code false} if none match or if the path is not a directory
	 *
	 * @throws NullPointerException if {@code path} or {@code descendantPredicate} is {@code null}
	 * @throws UncheckedIOException if an I/O error occurs while traversing the directory
	 */
	public static boolean hasDescendantMatching(Path path, Predicate<Path> descendantPredicate) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(descendantPredicate, "descendantPredicate is null");
		File file = path.toFile();
		if (!file.isDirectory()) {
			return false;
		}
		try (Stream<Path> stream = Files.walk(path)) {
			return stream
				.skip(1) // skip the root path itself
				.anyMatch(descendantPredicate);
		} catch (IOException e) {
			throw new UncheckedIOException("Exception while walking files of " + path, e);
		}
	}

	/**
	 * Tests whether the given path has at least one sibling that matches the provided predicate.
	 *
	 * <p>The siblings are the other entries in the same parent directory.
	 * The path itself is excluded from testing. If the path has no parent,
	 * the result is always {@code false}.
	 *
	 * @param path the path whose siblings should be tested (must not be {@code null})
	 * @param siblingPredicate the predicate to apply to each sibling (must not be {@code null})
	 *
	 * @return {@code true} if any sibling matches the predicate,
	 *         {@code false} if none match or if the path has no parent
	 *
	 * @throws NullPointerException if {@code path} or {@code siblingPredicate} is {@code null}
	 */
	public static boolean hasSiblingMatching(Path path, Predicate<Path> siblingPredicate) {
		Objects.requireNonNull(path, "path is null");
		Objects.requireNonNull(siblingPredicate, "siblingPredicate is null");
		Path parent = path.getParent();
		if (parent == null) {
			return false;
		}
		File[] siblings = parent.toFile().listFiles();
		if (siblings == null) { // Only if I/O error occurred when listing files
			return false;
		}
		for (File sibling : siblings) {
			if (!sibling.toPath().equals(path) && siblingPredicate.test(sibling.toPath())) {
				return true;
			}
		}
		return false;
	}

}
