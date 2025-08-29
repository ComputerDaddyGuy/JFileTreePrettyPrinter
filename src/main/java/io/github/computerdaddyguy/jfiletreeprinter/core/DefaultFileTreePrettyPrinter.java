package io.github.computerdaddyguy.jfiletreeprinter.core;

import io.github.computerdaddyguy.jfiletreeprinter.core.visitor.FileTreePrettyPrintVisitor;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultFileTreePrettyPrinter implements FileTreePrettyPrinter {

	private final FileTreePrettyPrintVisitor visitor;

	public DefaultFileTreePrettyPrinter(FileTreePrettyPrintVisitor visitor) {
		this.visitor = Objects.requireNonNull(visitor, "visitor cannot be null");
	}

	@Override
	public String prettyPrint(Path path) {
		try {
			visitor.reset();
			Files.walkFileTree(path, visitor);
			return visitor.getResult();
		} catch (IOException e) {
			throw new UncheckedIOException("Exception in pretty-print process of path: " + path.toAbsolutePath(), e);
		}
	}

}
