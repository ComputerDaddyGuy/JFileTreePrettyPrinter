package io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.depth.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.depth.DepthSymbol;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.TreeEntry.DirectoryListingExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.TreeEntry.DirectoryReadingAttributesExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.TreeEntry.FileReadingAttributesExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.TreeEntry.SkippedChildrenEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.LineRenderer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
class TreeEntryRenderer {

	private final PrettyPrintOptions options;
	private final LineRenderer lineRenderer;

	public TreeEntryRenderer(PrettyPrintOptions options, LineRenderer lineRenderer) {
		this.options = Objects.requireNonNull(options, "options is null");
		this.lineRenderer = Objects.requireNonNull(lineRenderer, "lineRenderer is null");
	}

	public String renderTree(TreeEntry entry) {
		var depth = Depth.createNewEmpty();
		var buff = new StringBuilder();
		renderTree(entry, depth, buff);
		return buff.toString();
	}

	public void renderTree(TreeEntry entry, Depth depth, StringBuilder buff) {
		switch (entry) {
			case TreeEntry.DirectoryEntry dirEntry -> renderDirectory(dirEntry, depth, buff, List.of(dirEntry.dir));
			case TreeEntry.FileEntry fileEntry -> renderFile(fileEntry, depth, buff);
			case TreeEntry.SkippedChildrenEntry skippedChildrenEntry -> renderSkippedChildrenEntry(skippedChildrenEntry, depth, buff);
			case TreeEntry.MaxDepthReachEntry maxDepthReachEntry -> renderMaxDepthReachEntry(maxDepthReachEntry, depth, buff);
			case TreeEntry.DirectoryReadingAttributesExceptionEntry dirReadingAttrsException -> renderDirReadingAttributesExceptionEntry(dirReadingAttrsException, depth, buff);
			case TreeEntry.DirectoryListingExceptionEntry dirListingException -> renderDirectoryListingExceptionEntry(dirListingException, depth, buff);
			case TreeEntry.FileReadingAttributesExceptionEntry fileReadingAttrsException -> renderFileReadingAttributesExceptionEntry(fileReadingAttrsException, depth, buff);
		}
	}

	private void renderDirectory(DirectoryEntry dirEntry, Depth depth, StringBuilder buff, List<Path> compactPaths) {

		if (options.areCompactDirectoriesUsed()) {
			if (dirEntry.entries.size() == 1 && dirEntry.entries.get(0) instanceof DirectoryEntry childDirEntry) {
				var newCompactPaths = new ArrayList<>(compactPaths);
				newCompactPaths.add(childDirEntry.dir);
				renderDirectory(childDirEntry, depth, buff, newCompactPaths);
				return;
			}
		}

		buff.append(lineRenderer.renderDirectoryBegin(depth, compactPaths));

		var childIt = dirEntry.entries.iterator();

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

	private void renderFile(FileEntry fileEntry, Depth depth, StringBuilder buff) {
		buff.append(lineRenderer.renderFile(depth, fileEntry.file, fileEntry.attrs));
	}

	private void renderSkippedChildrenEntry(SkippedChildrenEntry skippedChildrenEntry, Depth depth, StringBuilder buff) {
		buff.append(lineRenderer.renderChildrenLimitReached(depth, skippedChildrenEntry.skippedChildren));
	}

	private void renderMaxDepthReachEntry(MaxDepthReachEntry maxDepthReachEntry, Depth depth, StringBuilder buff) {
		buff.append(lineRenderer.renderMaxDepthReached(depth));
	}

	private void renderDirReadingAttributesExceptionEntry(DirectoryReadingAttributesExceptionEntry dirReadingAttrsException, Depth depth, StringBuilder buff) {
		buff.append(lineRenderer.renderDirectoryException(depth, dirReadingAttrsException.dir, dirReadingAttrsException.exception));
	}

	private void renderDirectoryListingExceptionEntry(DirectoryListingExceptionEntry dirListingException, Depth depth, StringBuilder buff) {
		buff.append(lineRenderer.renderDirectoryException(depth, dirListingException.dir, dirListingException.exception));
	}

	private void renderFileReadingAttributesExceptionEntry(FileReadingAttributesExceptionEntry fileReadingAttrsException, Depth depth, StringBuilder buff) {
		buff.append(lineRenderer.renderFileException(depth, fileReadingAttrsException.file, fileReadingAttrsException.exception));
	}

}
