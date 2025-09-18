package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.TreeEntryRenderer;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.PathToTreeScanner;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultFileTreePrettyPrinter implements FileTreePrettyPrinter {

	private final PathToTreeScanner scanner;
	private final TreeEntryRenderer renderer;

	public DefaultFileTreePrettyPrinter(PathToTreeScanner scanner, TreeEntryRenderer renderer) {
		this.scanner = Objects.requireNonNull(scanner, "scanner cannot be null");
		this.renderer = Objects.requireNonNull(renderer, "renderer cannot be null");
	}

	@Override
	public String prettyPrint(Path path, @Nullable Predicate<Path> filter) {
		var tree = scanner.scan(path, filter);
		return renderer.renderTree(tree);
	}

}
