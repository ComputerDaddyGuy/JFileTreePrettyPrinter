package io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.handler.PathToTreeMapper;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.LineRenderer;
import java.nio.file.Path;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RecursiveFileTreePrettyPrinter implements FileTreePrettyPrinter {

	private final PrettyPrintOptions options;

	public RecursiveFileTreePrettyPrinter(PrettyPrintOptions options) {
		this.options = Objects.requireNonNull(options, "options cannot be null");
	}

	@Override
	public String prettyPrint(Path path) {
		var depth = 0;
		var pathHandler = PathToTreeMapper.create(options);
		var tree = pathHandler.handle(depth, path);

		var lineRenderer = LineRenderer.create(options);
		var treeRenderer = new TreeEntryRenderer(options, lineRenderer);
		return treeRenderer.renderTree(tree);
	}

	@Override
	public PrettyPrintOptions getOptions() {
		return options;
	}

}
