package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.nio.file.Path;
import java.util.function.Predicate;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Pretty prints a file tree, and returns the result as a String.
 * 
 * @implNote Instances of this interface are not thread safe.
 */
@NullMarked
@FunctionalInterface
public interface FileTreePrettyPrinter {

	/**
	 * Pretty prints the given path.
	 * 
	 * @param path		A directory or a file.
	 * @param filter	Filter for paths to retain, <code>null</code> will retain all files. Applies only on children paths, not root path.
	 */
	String prettyPrint(Path path, @Nullable Predicate<Path> filter);

	/**
	 * Pretty prints the given path.
	 * 
	 * @param path	A directory or a file.
	 */
	default String prettyPrint(Path path) {
		return prettyPrint(path, null);
	}

	/**
	 * Pretty prints the given path.
	 * 
	 * @param path	A directory or a file.
	 */
	default String prettyPrint(String path) {
		return prettyPrint(path, null);
	}

	/**
	 * Pretty prints the given path.
	 * 
	 * @param path		A directory or a file.
	 * @param filter	Filter for paths to retain, <code>null</code> will retain all files. Applies only on children paths, not root path.
	 */
	default String prettyPrint(String path, @Nullable Predicate<Path> filter) {
		return prettyPrint(Path.of(path), filter);
	}

	/**
	 * Create a pretty printer with default options.
	 * 
	 * @see PrettyPrintOptions#createDefault()
	 */
	static FileTreePrettyPrinter createDefault() {
		return builder().build();
	}

	/**
	 * Customize creation of a pretty printer through a builder.
	 * 
	 * @see PrettyPrintOptions#createDefault()
	 */
	static FileTreePrettyPrinterBuilder builder() {
		return new FileTreePrettyPrinterBuilder();
	}

}
