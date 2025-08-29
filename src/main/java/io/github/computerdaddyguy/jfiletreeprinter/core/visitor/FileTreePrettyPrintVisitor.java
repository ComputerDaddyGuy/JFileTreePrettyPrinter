package io.github.computerdaddyguy.jfiletreeprinter.core.visitor;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

/**
 * @implNote Instances of this interface are not thread safe.
 */
@NullMarked
public interface FileTreePrettyPrintVisitor extends FileVisitor<Path> {

	/**
	 * Resets the visitor so it can be used again.
	 */
	void reset();

	/**
	 * Gets the file tree, pretty-printed.
	 * 
	 * @return
	 */
	String getResult();

	public static FileTreePrettyPrintVisitorBuilder builder() {
		return new FileTreePrettyPrintVisitorBuilder();
	}

}
