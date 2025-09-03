package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.RenderingOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.file.FileFormatter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.tree.TreeFormatter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface LineRenderer {

	@Nullable
	String renderDirectoryBegin(Depth depth, List<Path> dirs, BasicFileAttributes attrs);

	@Nullable
	String renderDirectoryException(Depth depth, Path dir, IOException exc);

	@Nullable
	String renderFile(Depth depth, Path file, BasicFileAttributes attrs);

	@Nullable
	String renderFileException(Depth depth, Path file, IOException exc);

	@Nullable
	String renderLimitReached(Depth depth, Set<Path> notVisited);

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
