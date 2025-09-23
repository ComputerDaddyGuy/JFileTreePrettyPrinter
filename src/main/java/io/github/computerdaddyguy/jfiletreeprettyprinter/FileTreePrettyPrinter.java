package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

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
	 * 
	 * @throws UncheckedIOException	If any IO error occurred
	 */
	String prettyPrint(Path path) throws UncheckedIOException;

	/**
	 * Pretty prints the given path.
	 * 
	 * @param path	A directory or a file.
	 * 
	 * @throws UncheckedIOException	If any IO error occurred
	 */
	default String prettyPrint(String path) throws UncheckedIOException {
		return prettyPrint(Path.of(path));
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
