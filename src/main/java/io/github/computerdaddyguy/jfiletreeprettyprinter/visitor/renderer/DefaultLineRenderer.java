package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.depth.DepthFormatter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.file.FileFormatter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultLineRenderer implements LineRenderer {

	private final DepthFormatter depthFormatter;
	private final FileFormatter fileFormatter;

	public DefaultLineRenderer(DepthFormatter depthFormatter, FileFormatter fileFormatter) {
		this.depthFormatter = Objects.requireNonNull(depthFormatter, "depthFormatter formatter is null");
		this.fileFormatter = Objects.requireNonNull(fileFormatter, "fileFormatter formatter is null");
	}

	@Override
	public String renderDirectoryBegin(Depth depth, Path dir, BasicFileAttributes attrs) {
		return depthFormatter.format(depth) + fileFormatter.formatDirectoryBegin(dir, attrs);
	}

	@Override
	public String renderDirectoryException(Depth depth, Path dir, IOException exc) {
		return depthFormatter.format(depth) + fileFormatter.formatDirectoryException(dir, exc);
	}

	@Override
	public String renderFile(Depth depth, Path file, BasicFileAttributes attrs) {
		return depthFormatter.format(depth) + fileFormatter.formatFile(file, attrs);
	}

	@Override
	public String renderFileException(Depth depth, Path file, IOException exc) {
		return depthFormatter.format(depth) + fileFormatter.formatFileException(file, exc);
	}

	@Override
	public @Nullable String renderLimitReached(Depth depth, Set<Path> notVisited) {
		return depthFormatter.format(depth) + fileFormatter.formatChildLimitReached(notVisited);
	}

}
