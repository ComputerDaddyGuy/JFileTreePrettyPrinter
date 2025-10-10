package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth.DepthFormatter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.nio.file.Path;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface LineRenderer {

	String renderDirectoryBegin(Depth depth, DirectoryEntry dirEntry, List<Path> dirs);

	String renderFile(Depth depth, FileEntry fileEntry);

	String renderChildLimitReached(Depth depth, SkippedChildrenEntry skippedChildrenEntry);

	String renderMaxDepthReached(Depth depth, MaxDepthReachEntry maxDepthReachEntry);

	/**
	 * Create a new line renderer, using given options
	 * @param useEmojis
	 * @return
	 */
	static LineRenderer create(RenderingOptions options) {
		var treeFormatter = DepthFormatter.getInstance(options.getTreeFormat());

		var fileFormatter = FileFormatter.createDefault();
		fileFormatter = FileFormatter.wrapWithEmojis(fileFormatter, options.getEmojiMapping());

		return new DefaultLineRenderer(treeFormatter, fileFormatter);
	}

}
