package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth.DepthFormatter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file.FileFormatter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryExceptionTreeEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileReadingAttributesExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.nio.file.Path;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
interface LineRenderer {

	@Nullable
	String renderDirectoryBegin(Depth depth, DirectoryEntry dirEntry, List<Path> dirs);

	@Nullable
	String renderDirectoryException(Depth depth, DirectoryExceptionTreeEntry dirExceptionEntry);

	@Nullable
	String renderFile(Depth depth, FileEntry fileEntry);

	@Nullable
	String renderFileException(Depth depth, FileReadingAttributesExceptionEntry fileReadingAttrsException);

	@Nullable
	String renderChildrenLimitReached(Depth depth, SkippedChildrenEntry skippedChildrenEntry);

	@Nullable
	String renderMaxDepthReached(Depth depth, MaxDepthReachEntry maxDepthReachEntry);

	/**
	 * Create a new line renderer, using given options
	 * @param useEmojis
	 * @return
	 */
	static LineRenderer create(RenderingOptions options) {
		var treeFormatter = DepthFormatter.getInstance(options.getTreeFormat());

		var fileFormatter = FileFormatter.createDefault();
		if (options.areEmojisUsed()) {
			fileFormatter = FileFormatter.wrapWithEmojis(fileFormatter);
		}

		return new DefaultLineRenderer(treeFormatter, fileFormatter);
	}

}
