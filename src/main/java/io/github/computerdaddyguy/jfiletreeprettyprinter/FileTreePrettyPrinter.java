package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

/**
 * Pretty prints a file tree, and returns the result as a String.
 * 
 * @implNote Instances of this interface are not thread safe.
 */
@NullMarked
public interface FileTreePrettyPrinter {

	/**
	 * Pretty prints the given path.
	 * 
	 * @param path	A directory or a file.
	 */
	String prettyPrint(Path path);

	/**
	 * Pretty prints the given path.
	 * 
	 * @param path	A directory or a file.
	 */
	default String prettyPrint(String path) {
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
