package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.nio.file.Path;
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
	public String renderDirectoryBegin(Depth depth, DirectoryEntry dirEntry, List<Path> dirs) {
		return treeFormatter.format(depth) + fileFormatter.formatDirectoryBegin(dirEntry, dirs);
	}

	@Override
	public String renderFile(Depth depth, FileEntry fileEntry) {
		return treeFormatter.format(depth) + fileFormatter.formatFile(fileEntry);
	}

	@Override
	public @Nullable String renderChildLimitReached(Depth depth, SkippedChildrenEntry skippedChildrenEntry) {
		return treeFormatter.format(depth) + fileFormatter.formatChildLimitReached(skippedChildrenEntry);
	}

	@Override
	public @Nullable String renderMaxDepthReached(Depth depth, MaxDepthReachEntry maxDepthReachEntry) {
		return treeFormatter.format(depth) + fileFormatter.formatMaxDepthReached(maxDepthReachEntry);
	}

}
