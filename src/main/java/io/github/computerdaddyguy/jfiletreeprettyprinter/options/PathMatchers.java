package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;

/**
 * Utility class providing factory and composition methods for {@link PathMatcher}s.
 *
 * <p>All methods are {@code static}. This class cannot be instantiated.</p>
 *
 * <h2>Design notes</h2>
 * <ul>
 *   <li>Unless otherwise stated, all methods throw {@link NullPointerException}
 *       if passed {@code null} arguments.</li>
 *   <li>{@link Path#getFileName()} may return {@code null} if the path is a root.
 *       This is always checked internally to avoid {@link NullPointerException}.</li>
 *   <li>Matchers generally normalize paths with {@link Path#normalize()} and
 *       use {@link Path#toAbsolutePath()} where absolute matching is intended.</li>
 * </ul>
 */

@NullMarked
public final class PathMatchers {

	/*
	 * --------------------------------------------------
	 * YES, Javadoc in this class has been AI written ;-)
	 * --------------------------------------------------
	 */

	private PathMatchers() {
		// Helper class
	}

	// ---------- PathMatcher combinations ----------

	/**
	 * Returns a matcher that negates the result of another matcher.
	 *
	 * @param matcher the matcher to negate
	 * @return a matcher returning {@code true} when the given matcher returns {@code false}
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher notTxt = PathMatchers.not(PathMatchers.hasExtension("txt"));
	 * notTxt.matches(Path.of("report.pdf")); // true
	 * notTxt.matches(Path.of("report.txt")); // false
	 * }</pre>
	 */
	public static PathMatcher not(PathMatcher matcher) {
		Objects.requireNonNull(matcher, "matcher is null");
		return path -> !matcher.matches(path);
	}

	/**
	 * Returns a matcher that requires <em>all</em> of the provided matchers to succeed.
	 *
	 * <p>The returned matcher evaluates the given matchers in sequence and stops
	 * at the first failure. If every matcher evaluates to {@code true}, the
	 * resulting matcher returns {@code true}.</p>
	 *
	 * @param matcher  a required first matcher (must not be {@code null})
	 * @param matchers additional matchers (zero or more, each must not be {@code null})
	 * @return a matcher returning {@code true} if all provided matchers return {@code true}
	 *
	 * @throws NullPointerException if any matcher is {@code null}
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher javaFilesInSrc =
	 *     PathMatchers.allOf(
	 *         PathMatchers.hasExtension("java"),
	 *         PathMatchers.hasAnyAncestorMatching(PathMatchers.hasName("src"))
	 *     );
	 * }</pre>
	 */
	public static PathMatcher allOf(PathMatcher matcher, PathMatcher... matchers) {
		return allOf(buildSafeList(matcher, matchers));
	}

	/**
	 * Returns a matcher that requires <em>all</em> of the provided matchers to succeed.
	 *
	 * <p>The returned matcher evaluates the matchers in sequence and stops
	 * at the first failure. If the iterable is empty, an
	 * {@link IllegalArgumentException} is thrown.</p>
	 *
	 * @param matchers the matchers to combine (must not be {@code null} or contain {@code null})
	 * @return a matcher returning {@code true} if all provided matchers return {@code true}
	 *
	 * @throws NullPointerException if {@code matchers} or any element is {@code null}
	 * @throws IllegalArgumentException if {@code matchers} is empty
	 */
	public static PathMatcher allOf(Iterable<PathMatcher> matchers) {
		return combineMatchers(matchers, Mode.ALL);
	}

	/**
	 * Returns a matcher that requires <em>none</em> of the provided matchers to succeed.
	 *
	 * <p>The returned matcher evaluates the given matchers in sequence and
	 * stops at the first success. It only returns {@code true} if all matchers return {@code false}.</p>
	 *
	 * @param matcher  a required first matcher (must not be {@code null})
	 * @param matchers additional matchers (zero or more, each must not be {@code null})
	 * @return a matcher returning {@code true} if none of the provided matchers return {@code true}
	 *
	 * @throws NullPointerException if any matcher is {@code null}
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher notHiddenOrBackup =
	 *     PathMatchers.noneOf(
	 *         PathMatchers.hasPrefix("."),
	 *         PathMatchers.hasSuffix("~")
	 *     );
	 * }</pre>
	 */
	public static PathMatcher noneOf(PathMatcher matcher, PathMatcher... matchers) {
		return noneOf(buildSafeList(matcher, matchers));
	}

	/**
	 * Returns a matcher that requires <em>none</em> of the provided matchers to succeed.
	 *
	 * <p>The returned matcher evaluates the matchers in sequence and stops
	 * at the first success. It only returns {@code true} if all matchers return {@code false}.
	 * If the iterable is empty, an {@link IllegalArgumentException} is thrown.</p>
	 *
	 * @param matchers the matchers to combine (must not be {@code null} or contain {@code null})
	 * @return a matcher returning {@code true} if none of the provided matchers return {@code true}
	 *
	 * @throws NullPointerException if {@code matchers} or any element is {@code null}
	 * @throws IllegalArgumentException if {@code matchers} is empty
	 */
	public static PathMatcher noneOf(Iterable<PathMatcher> matchers) {
		return combineMatchers(matchers, Mode.NONE);
	}

	/**
	 * Returns a matcher that requires <em>any</em> of the provided matchers to succeed.
	 *
	 * <p>The returned matcher evaluates the given matchers in sequence and stops
	 * at the first success. If at least one matcher evaluates to {@code true},
	 * the resulting matcher returns {@code true}.</p>
	 *
	 * @param matcher  a required first matcher (must not be {@code null})
	 * @param matchers additional matchers (zero or more, each must not be {@code null})
	 * @return a matcher returning {@code true} if at least one matcher returns {@code true}
	 *
	 * @throws NullPointerException if any matcher is {@code null}
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher images = PathMatchers.anyOf(
	 *     PathMatchers.hasExtension("png"),
	 *     PathMatchers.hasExtension("jpg"),
	 *     PathMatchers.hasExtension("gif")
	 * );
	 * }</pre>
	 */
	public static PathMatcher anyOf(PathMatcher matcher, PathMatcher... matchers) {
		return anyOf(buildSafeList(matcher, matchers));
	}

	/**
	 * Returns a matcher that requires <em>any</em> of the provided matchers to succeed.
	 *
	 * <p>The returned matcher evaluates the matchers in sequence and stops
	 * at the first success. If the iterable is empty, an
	 * {@link IllegalArgumentException} is thrown.</p>
	 *
	 * @param matchers the matchers to combine (must not be {@code null} or contain {@code null})
	 * @return a matcher returning {@code true} if at least one matcher returns {@code true}
	 *
	 * @throws NullPointerException if {@code matchers} or any element is {@code null}
	 * @throws IllegalArgumentException if {@code matchers} is empty
	 */
	public static PathMatcher anyOf(Iterable<PathMatcher> matchers) {
		return combineMatchers(matchers, Mode.ANY);
	}

	private static List<PathMatcher> buildSafeList(PathMatcher matcher, PathMatcher... matchers) {
		Objects.requireNonNull(matcher, "matcher is null");
		var list = new ArrayList<PathMatcher>(1 + matchers.length);
		list.add(matcher);
		for (PathMatcher m : matchers) {
			Objects.requireNonNull(m, "some matcher is null");
			list.add(m);
		}
		return List.copyOf(list);
	}

	private enum Mode {
		ALL,
		ANY,
		NONE
	}

	private static PathMatcher combineMatchers(Iterable<PathMatcher> matchers, Mode mode) {
		Objects.requireNonNull(matchers, "matchers is null");
		var list = new ArrayList<PathMatcher>();
		for (var m : matchers) {
			Objects.requireNonNull(m, "some matcher is null");
			list.add(m);
		}
		if (list.isEmpty()) {
			throw new IllegalArgumentException("No matcher provided");
		}
		return switch (mode) {
			case ALL -> path -> all(path, list);
			case ANY -> path -> any(path, list);
			case NONE -> path -> none(path, list);
		};
	}

	private static boolean all(Path path, List<PathMatcher> matchers) {
		for (PathMatcher m : matchers) {
			if (!m.matches(path))
				return false;
		}
		return true;
	}

	private static boolean any(Path path, List<PathMatcher> matchers) {
		for (PathMatcher m : matchers) {
			if (m.matches(path))
				return true;
		}
		return false;
	}

	private static boolean none(Path path, List<PathMatcher> matchers) {
		for (PathMatcher m : matchers) {
			if (m.matches(path))
				return false;
		}
		return true;
	}

	/**
	 * Returns a conditional matcher.
	 *
	 * <p>The returned matcher applies {@code ifMatcher}. If it matches, the
	 * {@code thenMatcher} is applied, otherwise the {@code elseMatcher} is applied.</p>
	 *
	 * @param ifMatcher condition to test
	 * @param thenMatcher matcher used if condition is true
	 * @param elseMatcher matcher used if condition is false
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher condition = PathMatchers.ifMatchesThenElse(
	 *     PathMatchers.isDirectory(),
	 *     PathMatchers.hasAnyDescendantMatching(PathMatchers.hasExtension("java")),
	 *     PathMatchers.hasExtension("txt")
	 * );
	 * }</pre>
	 */
	public static PathMatcher ifMatchesThenElse(PathMatcher ifMatcher, PathMatcher thenMatcher, PathMatcher elseMatcher) {
		Objects.requireNonNull(ifMatcher, "ifMatcher is null");
		Objects.requireNonNull(thenMatcher, "thenMatcher is null");
		Objects.requireNonNull(elseMatcher, "elseMatcher is null");
		return path -> ifMatcher.matches(path) ? thenMatcher.matches(path) : elseMatcher.matches(path);
	}

	// ---------- Glob ----------

	/**
	 * Creates a matcher testing the <em>absolute, normalized</em> path string
	 * against a glob expression.
	 *
	 * <p>The glob syntax is the same as accepted by
	 * {@link java.nio.file.FileSystem#getPathMatcher(String)} with a "glob:" prefix.</p>
	 *
	 * <p>Special case: if {@code glob} is exactly "*", the matcher always returns {@code true}.</p>
	 *
	 * @param glob glob pattern (e.g. {@code "**\*.java"}, {@code "*.log"})
	 * @return a matcher applying the glob to {@link Path#toAbsolutePath()}
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher matcher = PathMatchers.hasAbsolutePathMatchingGlob("**\*.java");
	 * matcher.matches(Path.of("/home/user/project/Main.java")); // true
	 * matcher.matches(Path.of("/home/user/project/notes.txt")); // false
	 * }</pre>
	 */
	public static PathMatcher hasAbsolutePathMatchingGlob(String glob) {
		Objects.requireNonNull(glob, "glob is null");

		// Optimization from Files.newDirectoryStream(Path dir, String glob)
		if (glob.equals("*")) {
			return path -> true;
		}

		// Evaluate the glob pattern only once, not on every matcher evaluation
		var matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
		return hasAbsolutePathMatching(matcher);
	}

	/**
	 * Creates a matcher testing the <em>relative path</em> of a path
	 * with respect to a reference directory against a glob expression.
	 *
	 * <p>Both paths are normalized and made absolute before relativization.</p>
	 *
	 * <p>Special case: if {@code glob} is exactly "*", the matcher always returns {@code true}.</p>
	 *
	 * @param ref the reference path to relativize against
	 * @param glob glob pattern (e.g. {@code "*.md"})
	 * @return a matcher applying the glob to {@code ref.relativize(path)}
	 *
	 * @throws IllegalArgumentException if {@code ref} and {@code path} are on different roots
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * Path base = Path.of("/home/user/project");
	 * PathMatcher matcher = PathMatchers.hasRelativePathMatchingGlob(base, "**\*.java");
	 *
	 * matcher.matches(Path.of("/home/user/project/src/Main.java")); // true
	 * matcher.matches(Path.of("/tmp/file.java")); // throws IllegalArgumentException
	 * }</pre>
	 */
	public static PathMatcher hasRelativePathMatchingGlob(Path ref, String glob) {
		Objects.requireNonNull(glob, "glob is null");
		Objects.requireNonNull(ref, "ref is null");

		// Optimization from Files.newDirectoryStream(Path dir, String glob)
		if (glob.equals("*")) {
			return path -> true;
		}

		// Evaluate the glob pattern only once, not on every matcher evaluation
		var matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
		return hasRelativePathMatching(ref, matcher);
	}

	/**
	 * Creates a matcher that applies another matcher to the path’s
	 * absolute, normalized form.
	 *
	 * @param matcher matcher to apply
	 * @return a matcher applying {@code matcher.matches(path.toAbsolutePath().normalize())}
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher endsWithPom = PathMatchers.hasAbsolutePathMatching(
	 *     PathMatchers.hasName("pom.xml")
	 * );
	 * }</pre>
	 */
	public static PathMatcher hasAbsolutePathMatching(PathMatcher matcher) {
		Objects.requireNonNull(matcher, "matcher is null");
		return path -> matcher.matches(path.normalize().toAbsolutePath());
	}

	/**
	 * Creates a matcher that applies another matcher to the relative path
	 * between a reference and the tested path.
	 *
	 * <p>Both paths are normalized and made absolute before relativization.</p>
	 *
	 * @param ref reference path
	 * @param matcher matcher to apply to {@code ref.relativize(path)}
	 * @return matcher applying {@code matcher} to the relative path
	 *
	 * @throws IllegalArgumentException if {@code ref} and {@code path} are on different roots
	 */
	public static PathMatcher hasRelativePathMatching(Path ref, PathMatcher matcher) {
		Objects.requireNonNull(matcher, "matcher is null");
		Objects.requireNonNull(ref, "ref is null");
		final var cleanedRef = ref.normalize().toAbsolutePath();
		return path -> {
			final var cleanedPath = path.normalize().toAbsolutePath();
			if (!cleanedPath.getRoot().equals(cleanedRef.getRoot())) {
				return false; // paths are not on the same root (e.g. C:/... vs D:/... on Windows)
			}
			var relativePath = cleanedRef.relativize(cleanedPath);
			return matcher.matches(relativePath);
		};
	}

	// ---------- Name ----------

	private static final boolean hasFileName(Path path) {
		return path.getFileName() != null;
	}

	/**
	 * Matches paths whose file name is exactly equal to {@code name}.
	 *
	 * <p>If {@link Path#getFileName()} returns {@code null} (root paths),
	 * this matcher always returns {@code false}.</p>
	 *
	 * @param name exact expected file name
	 * @return matcher returning true if {@code path.getFileName().toString().equals(name)}
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher matcher = PathMatchers.hasName("config.yaml");
	 * matcher.matches(Path.of("/etc/config.yaml")); // true
	 * matcher.matches(Path.of("/etc/CONFIG.YAML")); // false
	 * }</pre>
	 */
	public static PathMatcher hasName(String name) {
		Objects.requireNonNull(name, "name is null");
		return path -> hasFileName(path) && path.getFileName().toString().equals(name);
	}

	/**
	 * Matches paths whose file name is equal to {@code name}, ignoring case.
	 *
	 * <p>If {@link Path#getFileName()} returns {@code null} (root paths),
	 * this matcher always returns {@code false}.</p>
	 *
	 * @param name expected file name (case-insensitive)
	 * @return matcher returning true if names are equal ignoring case
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher matcher = PathMatchers.hasNameIgnoreCase("config.yaml");
	 * matcher.matches(Path.of("/etc/config.yaml")); // true
	 * matcher.matches(Path.of("/etc/CONFIG.YAML")); // true
	 * }</pre>
	 */
	public static PathMatcher hasNameIgnoreCase(String name) {
		Objects.requireNonNull(name, "name is null");
		return path -> hasFileName(path) && path.getFileName().toString().equalsIgnoreCase(name);
	}

	/**
	 * Matches paths whose file name matches a regular expression.
	 *
	 * <p>The regex is tested using {@link Pattern#matcher(CharSequence)}.</p>
	 *
	 * <p>If {@link Path#getFileName()} returns {@code null} (root paths),
	 * this matcher always returns {@code false}.</p>
	 *
	 * @param pattern regex pattern
	 * @return matcher returning true if file name matches the regex
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * Pattern p = Pattern.compile("data-\\d+\\.csv");
	 * PathMatcher matcher = PathMatchers.hasNameMatching(p);
	 * matcher.matches(Path.of("data-123.csv")); // true
	 * matcher.matches(Path.of("data.txt")); // false
	 * }</pre>
	 */
	public static PathMatcher hasNameMatching(Pattern pattern) {
		Objects.requireNonNull(pattern, "pattern is null");
		return path -> hasFileName(path) && pattern.matcher(path.getFileName().toString()).matches();
	}

	/**
	 * Matches paths whose file name matches a glob expression.
	 *
	 * <p>The glob is evaluated by {@link java.nio.file.FileSystem#getPathMatcher(String)}.</p>
	 *
	 * <p>If {@link Path#getFileName()} returns {@code null} (root paths),
	 * this matcher always returns {@code false}.</p>
	 *
	 * <p>Special case: if glob is exactly "*", the matcher always returns {@code true}.</p>
	 *
	 * @param glob glob pattern (e.g. {@code "*.txt"})
	 * @return matcher testing the file name against the glob
	 */
	public static PathMatcher hasNameMatchingGlob(String glob) {
		Objects.requireNonNull(glob, "glob is null");

		// Optimization from Files.newDirectoryStream(Path dir, String glob)
		if (glob.equals("*")) {
			return path -> true;
		}

		// Evaluate the glob pattern only once, not on every matcher evaluation
		var matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
		return path -> hasFileName(path) && matcher.matches(path.getFileName());
	}

	/**
	 * Matches paths whose file name starts with a prefix.
	 *
	 * <p>If {@link Path#getFileName()} returns {@code null}, this matcher
	 * always returns {@code false}.</p>
	 *
	 * @param prefix required prefix (e.g. ".", "log-")
	 * @return matcher returning true if file name starts with prefix
	 */
	public static PathMatcher hasNameStartingWith(String prefix) {
		Objects.requireNonNull(prefix, "prefix is null");
		return path -> hasFileName(path) && path.getFileName().toString().startsWith(prefix);
	}

	/**
	 * Matches paths whose file name ends with a suffix.
	 *
	 * <p>If {@link Path#getFileName()} returns {@code null}, this matcher
	 * always returns {@code false}.</p>
	 *
	 * @param suffix required suffix (e.g. ".log", ".bak")
	 * @return matcher returning true if file name ends with suffix
	 */
	public static PathMatcher hasNameEndingWith(String suffix) {
		Objects.requireNonNull(suffix, "suffix is null");
		return path -> hasFileName(path) && path.getFileName().toString().endsWith(suffix);
	}

	/**
	 * Matches paths whose file name ends with the given extension.
	 *
	 * <p>The extension must be provided without a leading dot.</p>
	 * <p>If {@link Path#getFileName()} returns {@code null}, this matcher
	 * always returns {@code false}.</p>
	 *
	 * @param extension required extension (without dot)
	 * @return matcher returning true if file name ends with ".{extension}"
	 *
	 * <p>Example:</p>
	 * <pre>{@code
	 * PathMatcher matcher = PathMatchers.hasExtension("txt");
	 * matcher.matches(Path.of("notes.txt")); // true
	 * matcher.matches(Path.of("notes.TXT")); // false
	 * }</pre>
	 */
	public static PathMatcher hasExtension(String extension) {
		Objects.requireNonNull(extension, "extension is null");
		return hasNameEndingWith("." + extension);
	}

	// ---------- Type ----------

	/**
	 * Matches paths that are directories.
	 *
	 * <p>This calls {@link Files#isDirectory(Path, LinkOption...)} with
	 * {@link LinkOption#NOFOLLOW_LINKS}. Symbolic links are therefore
	 * not considered directories even if they point to one.</p>
	 *
	 * @return matcher returning true if the path is a directory
	 */
	public static PathMatcher isDirectory() {
		return path -> Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
	}

	/**
	 * Matches paths that are files (not directories).
	 *
	 * <p>This is defined as {@code not(isDirectory())}. It will return {@code true}
	 * for regular files, symbolic links, and special filesystem objects such as sockets,
	 * device files, or FIFOs. In other words, anything that is not a directory is considered
	 * a "file" here.</p>
	 *
	 * <p>⚠ Note: If you specifically need to test for <em>regular files</em>,
	 * use {@link #isRegularFile()}. If you specifically need to test for symbolic links,
	 * use {@link #isSymbolicLink()}.</p>
	 *
	 * @return matcher returning {@code true} if the path is not a directory
	 */
	public static PathMatcher isFile() {
		return not(isDirectory());
	}

	/**
	 * Matches paths that are <em>regular files</em>.
	 *
	 * <p>This method delegates to {@link Files#isRegularFile(Path, LinkOption...)}
	 * with {@link LinkOption#NOFOLLOW_LINKS}. Symbolic links, directories, and
	 * special files will return {@code false}.</p>
	 *
	 * @return matcher returning {@code true} if the path is a regular file
	 */
	public static PathMatcher isRegularFile() {
		return path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS);
	}

	/**
	 * Matches paths that are symbolic links.
	 *
	 * <p>This method delegates to {@link Files#isSymbolicLink(Path)}.</p>
	 *
	 * @return matcher returning {@code true} if the path is a symbolic link
	 */
	public static PathMatcher isSymbolicLink() {
		return Files::isSymbolicLink;
	}

	// ---------- Hierarchy ----------

	/**
	 * Matches paths whose <em>direct parent</em> matches another matcher.
	 *
	 * <p>If the path has no parent (root paths), this matcher always returns false.</p>
	 *
	 * @param parentMatcher matcher to apply to the parent
	 * @return matcher testing the direct parent
	 */
	public static PathMatcher hasDirectParentMatching(PathMatcher parentMatcher) {
		Objects.requireNonNull(parentMatcher, "parentMatcher is null");

		return path -> path.getParent() != null && parentMatcher.matches(path.getParent());
	}

	/**
	 * Matches paths having <em>any ancestor</em> that matches another matcher.
	 *
	 * <p>Ancestors are tested by walking up the chain of {@link Path#getParent()}.
	 * The search stops at the root or when a match is found.</p>
	 *
	 * @param ancestorMatcher matcher to apply to each ancestor
	 * @return matcher returning true if any ancestor matches
	 */
	public static PathMatcher hasAnyAncestorMatching(PathMatcher ancestorMatcher) {
		Objects.requireNonNull(ancestorMatcher, "ancestorMatcher is null");

		return path -> {
			Path parent = path.getParent();
			while (parent != null) {
				if (ancestorMatcher.matches(parent)) {
					return true;
				}
				parent = parent.getParent();
			}
			return false;
		};
	}

	/**
	 * Matches directories that have <em>at least one direct child</em>
	 * matching another matcher.
	 *
	 * <p>Non-directories always return false. Only immediate children
	 * are tested (depth = 1).</p>
	 *
	 * @param childMatcher matcher applied to children
	 * @return matcher returning true if any direct child matches
	 */
	public static PathMatcher hasAnyDirectChildMatching(PathMatcher childMatcher) {
		Objects.requireNonNull(childMatcher, "childMatcher is null");
		return path -> testDescendants(path, 1, childMatcher, p -> true);
	}

	/**
	 * Matches directories that have <em>at least one descendant</em>
	 * (child, grandchild, etc.) matching another matcher.
	 *
	 * <p>Non-directories always return false. The directory itself
	 * is excluded from testing.</p>
	 *
	 * @param descendantMatcher matcher applied to descendants
	 * @return matcher returning true if any descendant matches
	 *
	 * @throws UncheckedIOException if an I/O error occurs while walking
	 */
	public static PathMatcher hasAnyDescendantMatching(PathMatcher descendantMatcher) {
		Objects.requireNonNull(descendantMatcher, "descendantMatcher is null");
		return path -> testDescendants(path, Integer.MAX_VALUE, descendantMatcher, p -> true);
	}

	/**
	 * Matches paths that have <em>at least one sibling</em> matching another matcher.
	 *
	 * <p>Siblings are other entries in the same parent directory.
	 * The path itself is excluded from testing.</p>
	 *
	 * <p>If the path has no parent (is root), this matcher always returns false.</p>
	 *
	 * @param siblingMatcher matcher applied to siblings
	 * @return matcher returning true if any sibling matches
	 */
	public static PathMatcher hasSiblingMatching(PathMatcher siblingMatcher) {
		Objects.requireNonNull(siblingMatcher, "siblingMatcher is null");
		return path -> path.getParent() != null && testDescendants(path.getParent(), 1, siblingMatcher, sibling -> !sibling.equals(path));
	}

	private static final boolean testDescendants(Path path, int depth, PathMatcher descendantMatcher, Predicate<Path> inclusionFilter) {
		if (!isDirectory().matches(path)) {
			return false;
		}
		try (Stream<Path> stream = Files.walk(path, depth)) { // Files.walk() do NOT follow symlink by default
			return stream
				.skip(1) // skip the root path itself
				.filter(inclusionFilter)
				.anyMatch(descendantMatcher::matches);
		} catch (IOException e) {
			throw new UncheckedIOException("Exception while walking files of " + path, e);
		}
	}

}
