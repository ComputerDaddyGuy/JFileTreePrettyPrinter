package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.TreeEntryRenderer;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.PathToTreeScanner;
import java.nio.file.Path;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultFileTreePrettyPrinter implements FileTreePrettyPrinter {

	private final PathToTreeScanner scanner;
	private final TreeEntryRenderer renderer;

	public DefaultFileTreePrettyPrinter(PathToTreeScanner scanner, TreeEntryRenderer renderer) {
		this.scanner = Objects.requireNonNull(scanner, "scanner cannot be null");
		this.renderer = Objects.requireNonNull(renderer, "renderer cannot be null");
	}

	@Override
	public String prettyPrint(Path path) {
		Objects.requireNonNull(path, "path cannot be null");
		path = path.toAbsolutePath().normalize();
		var tree = scanner.scan(path);
		return renderer.renderTree(tree);
	}

}
