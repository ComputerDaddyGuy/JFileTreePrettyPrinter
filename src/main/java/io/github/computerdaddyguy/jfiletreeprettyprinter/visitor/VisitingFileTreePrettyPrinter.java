package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class VisitingFileTreePrettyPrinter implements FileTreePrettyPrinter {

	private final PrettyPrintOptions options;

	public VisitingFileTreePrettyPrinter(PrettyPrintOptions options) {
		this.options = Objects.requireNonNull(options, "options cannot be null");
	}

	@Override
	public String prettyPrint(Path path) {
		try {
			var visitor = FileTreePrettyPrintVisitor.create(options);
			Files.walkFileTree(path, visitor);
			return visitor.getResult();
		} catch (IOException e) {
			throw new UncheckedIOException("Exception in pretty-print process of path: " + path.toAbsolutePath(), e);
		}
	}

	@Override
	public PrettyPrintOptions getOptions() {
		return null;
	}

}
