package io.github.computerdaddyguy.jfiletreeprettyprinter.impl.visitor;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.LineRenderer;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

/**
 * @implNote Instances of this interface are not thread safe.
 */
@NullMarked
public interface FileTreePrettyPrintVisitor extends FileVisitor<Path> {

	/**
	 * Gets the file tree, pretty-printed.
	 * 
	 * @return
	 */
	String getResult();

	public static FileTreePrettyPrintVisitor create(PrettyPrintOptions options) {
		var lineRenderer = LineRenderer.create(options);
		return new DefaultFileTreePrettyPrintVisitor(options, lineRenderer);
	}

}
