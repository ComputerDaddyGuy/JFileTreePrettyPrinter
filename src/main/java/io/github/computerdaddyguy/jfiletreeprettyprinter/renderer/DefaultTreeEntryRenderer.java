package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth.DepthSymbol;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryExceptionTreeEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileReadingAttributesExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultTreeEntryRenderer implements TreeEntryRenderer {

	private final RenderingOptions options;
	private final LineRenderer lineRenderer;

	public DefaultTreeEntryRenderer(RenderingOptions options) {
		this.options = Objects.requireNonNull(options, "options is null");
		this.lineRenderer = LineRenderer.create(options);
	}

	@Override
	public String renderTree(TreeEntry entry) {
		var depth = Depth.createNewEmpty();
		var buff = new StringBuilder();
		renderTree(entry, depth, buff);
		return buff.toString();
	}

	public void renderTree(TreeEntry entry, Depth depth, StringBuilder buff) {
		switch (entry) {
			case TreeEntry.DirectoryEntry dirEntry -> renderDirectory(buff, depth, dirEntry, List.of(dirEntry.getDir()));
			case TreeEntry.FileEntry fileEntry -> renderFile(buff, depth, fileEntry);
			case TreeEntry.SkippedChildrenEntry skippedChildrenEntry -> renderSkippedChildrenEntry(buff, depth, skippedChildrenEntry);
			case TreeEntry.MaxDepthReachEntry maxDepthReachEntry -> renderMaxDepthReachEntry(buff, depth, maxDepthReachEntry);
			case TreeEntry.DirectoryExceptionTreeEntry dirExceptionEntry -> renderDirExceptionEntry(buff, depth, dirExceptionEntry);
			case TreeEntry.FileReadingAttributesExceptionEntry fileReadingAttrsException -> renderFileReadingAttributesExceptionEntry(buff, depth, fileReadingAttrsException);
		}
	}

	private void renderDirectory(StringBuilder buff, Depth depth, DirectoryEntry dirEntry, List<Path> compactPaths) {

		if (options.areCompactDirectoriesUsed()) {
			if (dirEntry.getEntries().size() == 1 && dirEntry.getEntries().get(0) instanceof DirectoryEntry childDirEntry) {
				var newCompactPaths = new ArrayList<>(compactPaths);
				newCompactPaths.add(childDirEntry.getDir());
				renderDirectory(buff, depth, childDirEntry, newCompactPaths);
				return;
			}
		}

		buff.append(lineRenderer.renderDirectoryBegin(depth, dirEntry, compactPaths));

		var childIt = dirEntry.getEntries().iterator();

		if (childIt.hasNext()) {
			buff.append('\n');
		}

		while (childIt.hasNext()) {
			var childEntry = childIt.next();
			var childDepth = depth.append(childIt.hasNext() ? DepthSymbol.NON_LAST_FILE : DepthSymbol.LAST_FILE);
			renderTree(childEntry, childDepth, buff);
			if (childIt.hasNext()) {
				buff.append('\n');
			}
		}
	}

	private void renderFile(StringBuilder buff, Depth depth, FileEntry fileEntry) {
		buff.append(lineRenderer.renderFile(depth, fileEntry));
	}

	private void renderSkippedChildrenEntry(StringBuilder buff, Depth depth, SkippedChildrenEntry skippedChildrenEntry) {
		buff.append(lineRenderer.renderChildrenLimitReached(depth, skippedChildrenEntry));
	}

	private void renderMaxDepthReachEntry(StringBuilder buff, Depth depth, MaxDepthReachEntry maxDepthReachEntry) {
		buff.append(lineRenderer.renderMaxDepthReached(depth, maxDepthReachEntry));
	}

	private void renderDirExceptionEntry(StringBuilder buff, Depth depth, DirectoryExceptionTreeEntry dirExceptionEntry) {
		buff.append(lineRenderer.renderDirectoryException(depth, dirExceptionEntry));
	}

	private void renderFileReadingAttributesExceptionEntry(StringBuilder buff, Depth depth, FileReadingAttributesExceptionEntry fileReadingAttrsException) {
		buff.append(lineRenderer.renderFileException(depth, fileReadingAttrsException));
	}

}
