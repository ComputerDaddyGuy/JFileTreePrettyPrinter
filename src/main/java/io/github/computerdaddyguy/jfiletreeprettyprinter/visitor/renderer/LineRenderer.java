package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.RenderingOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.depth.DepthFormatter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.file.FileFormatter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface LineRenderer {

	@Nullable
	String renderDirectoryBegin(Depth depth, Path dir, BasicFileAttributes attrs);

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
		var depthFormatter = DepthFormatter.getInstance(options.depthFormat());

		var fileFormatter = FileFormatter.createDefault();
		if (options.useEmojis()) {
			fileFormatter = FileFormatter.wrapWithEmojis(fileFormatter);
		}

		return new DefaultLineRenderer(depthFormatter, fileFormatter);
	}

}
