package io.github.computerdaddyguy.jfiletreeprinter.core;

import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.FileTreePrettyPrintVisitor;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

/**
 * @implNote Instances of this interface are not thread safe.
 */
@NullMarked
public interface FileTreePrettyPrinter {

	String prettyPrint(Path path);

	default String prettyPrint(String path) {
		return prettyPrint(Path.of(path));
	}

	static FileTreePrettyPrinter createDefault() {
		var visitor = FileTreePrettyPrintVisitor.builder().build();
		return new DefaultFileTreePrettyPrinter(visitor);
	}

	static FileTreePrettyPrinter createWithVisitor(FileTreePrettyPrintVisitor visitor) {
		return new DefaultFileTreePrettyPrinter(visitor);
	}

}
