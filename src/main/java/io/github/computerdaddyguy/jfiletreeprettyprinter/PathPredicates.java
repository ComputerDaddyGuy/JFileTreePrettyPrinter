package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PathPredicates {

	private PathPredicates() {
		// Helper class
	}

	public static boolean hasName(Path path, String name) {
		return hasName(name).test(path);
	}

	public static Predicate<Path> hasName(String name) {
		return p -> p.getFileName().toString().equals(name);
	}

	public static boolean hasNameIgnoreCase(Path path, String name) {
		return hasNameIgnoreCase(name).test(path);
	}

	public static Predicate<Path> hasNameIgnoreCase(String name) {
		return p -> p.getFileName().toString().equalsIgnoreCase(name);
	}

	public static boolean hasNameMatching(Path path, Pattern pattern) {
		return hasNameMatching(pattern).test(path);
	}

	public static Predicate<Path> hasNameMatching(Pattern pattern) {
		return p -> pattern.matcher(p.getFileName().toString()).matches();
	}

	public static boolean hasNameEndingWith(Path path, String suffix) {
		return hasNameEndingWith(suffix).test(path);
	}

	public static Predicate<Path> hasNameEndingWith(String suffix) {
		return p -> p.getFileName().toString().endsWith(suffix);
	}

	/**
	 * Test if the file name ends with ".<extension>".
	 * @param extension	The extension to test, without the dot (ex: "txt", "pdf", etc.)
	 */
	public static boolean hasExtension(Path path, String extension) {
		return hasExtension(extension).test(path);
	}

	/**
	 * Create a predicate to test if the file name ends with ".<extension>".
	 * @param extension	The extension to test, without the dot (ex: "txt", "pdf", etc.)
	 */
	public static Predicate<Path> hasExtension(String extension) {
		return hasNameEndingWith("." + extension);
	}

}
