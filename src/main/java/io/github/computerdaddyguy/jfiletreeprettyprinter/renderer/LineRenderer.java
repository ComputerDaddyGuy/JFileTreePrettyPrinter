package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.depth.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.RenderingOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file.FileFormatter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.tree.TreeFormatter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface LineRenderer {

	@Nullable
	String renderDirectoryBegin(Depth depth, List<Path> dirs);

	@Nullable
	String renderDirectoryException(Depth depth, Path dir, IOException exc);

	@Nullable
	String renderFile(Depth depth, Path file, BasicFileAttributes attrs);

	@Nullable
	String renderFileException(Depth depth, Path file, IOException exc);

	@Nullable
	String renderChildrenLimitReached(Depth depth, Collection<Path> notVisited);

	@Nullable
	String renderMaxDepthReached(Depth depth);

	/**
	 * Create a new line renderer, using given options
	 * @param useEmojis
	 * @return
	 */
	static LineRenderer create(RenderingOptions options) {
		var treeFormatter = TreeFormatter.getInstance(options.getTreeFormat());

		var fileFormatter = FileFormatter.createDefault();
		if (options.areEmojisUsed()) {
			fileFormatter = FileFormatter.wrapWithEmojis(fileFormatter);
		}

		return new DefaultLineRenderer(treeFormatter, fileFormatter);
	}

}
