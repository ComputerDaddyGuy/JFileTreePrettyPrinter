package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.nio.file.Path;
import java.util.regex.Pattern;

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
public final class PathUtils {

	private PathUtils() {
		// Helper class
	}

	// ---------- Name ----------

	/**
	 * Tests whether the given path has exactly the specified file name.
	 * 
	 * @param path the path to test
	 * @param name the expected file name (without parent directories)
	 * 
	 * @return {@code true} if the path's file name equals {@code name}
	 */
	public static boolean hasName(Path path, String name) {
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
	 */
	public static boolean hasNameIgnoreCase(Path path, String name) {
		return path.getFileName().toString().equalsIgnoreCase(name);
	}

	/**
	 * Tests whether the given path's file name matches the provided pattern.
	 *
	 * @param path the path to test
	 * @param pattern the regex pattern to apply to the file name
	 * 
	 * @return {@code true} if the file name matches the pattern
	 */
	public static boolean hasNameMatching(Path path, Pattern pattern) {
		return pattern.matcher(path.getFileName().toString()).matches();
	}

	/**
	 * Tests whether the given path's file name ends with the specified suffix.
	 *
	 * @param path the path to test
	 * @param suffix the suffix to test (e.g. ".log", ".txt")
	 * 
	 * @return {@code true} if the file name ends with the given suffix
	 */
	public static boolean hasNameEndingWith(Path path, String suffix) {
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
	 */
	public static boolean hasExtension(Path path, String extension) {
		return hasNameEndingWith(path, "." + extension);
	}

	// ---------- Type ----------

	/**
	 * Tests whether the given path represents a directory.
	 *
	 * @param path the path to test
	 * 
	 * @return {@code true} if the path is a directory
	 */
	public static boolean isDirectory(Path path) {
		return path.toFile().isDirectory();
	}

	/**
	 * Tests whether the given path represents a file.
	 *
	 * @param path the path to test
	 * 
	 * @return {@code true} if the path is a file
	 */
	public static boolean isFile(Path path) {
		return path.toFile().isFile();
	}

}
