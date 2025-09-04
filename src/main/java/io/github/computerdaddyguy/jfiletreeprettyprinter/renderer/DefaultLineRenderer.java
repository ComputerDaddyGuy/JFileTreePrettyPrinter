package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.depth.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file.FileFormatter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.tree.TreeFormatter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultLineRenderer implements LineRenderer {

	private final TreeFormatter treeFormatter;
	private final FileFormatter fileFormatter;

	public DefaultLineRenderer(TreeFormatter treeFormatter, FileFormatter fileFormatter) {
		this.treeFormatter = Objects.requireNonNull(treeFormatter, "treeFormatter formatter is null");
		this.fileFormatter = Objects.requireNonNull(fileFormatter, "fileFormatter formatter is null");
	}

	@Override
	public String renderDirectoryBegin(Depth depth, List<Path> dirs) {
		return treeFormatter.format(depth) + fileFormatter.formatDirectoryBegin(dirs);
	}

	@Override
	public String renderDirectoryException(Depth depth, Path dir, IOException exc) {
		return treeFormatter.format(depth) + fileFormatter.formatDirectoryException(dir, exc);
	}

	@Override
	public String renderFile(Depth depth, Path file, BasicFileAttributes attrs) {
		return treeFormatter.format(depth) + fileFormatter.formatFile(file, attrs);
	}

	@Override
	public String renderFileException(Depth depth, Path file, IOException exc) {
		return treeFormatter.format(depth) + fileFormatter.formatFileException(file, exc);
	}

	@Override
	public @Nullable String renderChildrenLimitReached(Depth depth, Collection<Path> notVisited) {
		return treeFormatter.format(depth) + fileFormatter.formatChildLimitReached(notVisited);
	}

	@Override
	public @Nullable String renderMaxDepthReached(Depth depth) {
		return treeFormatter.format(depth) + fileFormatter.formatMaxDepthReached(depth);
	}

}
