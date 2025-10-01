package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth.DepthSymbol;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
		return renderTree(entry, Depth.createNewEmpty());
	}

	private String renderTree(TreeEntry entry, Depth depth) {
		return switch (entry) {
			case TreeEntry.DirectoryEntry dirEntry -> renderDirectory(depth, dirEntry, List.of(dirEntry.getDir()));
			case TreeEntry.FileEntry fileEntry -> renderFile(depth, fileEntry);
			case TreeEntry.SkippedChildrenEntry skippedChildrenEntry -> renderSkippedChildrenEntry(depth, skippedChildrenEntry);
			case TreeEntry.MaxDepthReachEntry maxDepthReachEntry -> renderMaxDepthReachEntry(depth, maxDepthReachEntry);
		};
	}

	private String renderDirectory(Depth depth, DirectoryEntry dirEntry, List<Path> compactPaths) {

		Optional<String> extension = null;
		if (options.areCompactDirectoriesUsed()
			&& !depth.isRoot()
			&& dirEntry.getEntries().size() == 1
			&& dirEntry.getEntries().get(0) instanceof DirectoryEntry childDirEntry) {

			extension = computeLineExtension(dirEntry.getDir());
			if (extension.isEmpty()) {
				var newCompactPaths = new ArrayList<>(compactPaths);
				newCompactPaths.add(childDirEntry.getDir());
				return renderDirectory(depth, childDirEntry, newCompactPaths);
			}
		}

		var line = lineRenderer.renderDirectoryBegin(depth, dirEntry, compactPaths);
		if (extension == null) {
			extension = computeLineExtension(dirEntry.getDir());
		}
		line += extension.orElse("");

		var childIt = dirEntry.getEntries().iterator();

		if (childIt.hasNext()) {
			line += "\n";
		}

		var childLines = new StringBuilder();

		while (childIt.hasNext()) {
			var childEntry = childIt.next();
			var childDepth = depth.append(childIt.hasNext() ? DepthSymbol.NON_LAST_FILE : DepthSymbol.LAST_FILE);
			childLines.append(renderTree(childEntry, childDepth));
			if (childIt.hasNext()) {
				childLines.append('\n');
			}
		}

		return line + childLines.toString();
	}

	private Optional<String> computeLineExtension(Path path) {
		if (options.getLineExtension() == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(options.getLineExtension().apply(path));
	}

	private String renderFile(Depth depth, FileEntry fileEntry) {
		var line = lineRenderer.renderFile(depth, fileEntry);
		line += computeLineExtension(fileEntry.getFile()).orElse("");
		return line;
	}

	private String renderSkippedChildrenEntry(Depth depth, SkippedChildrenEntry skippedChildrenEntry) {
		return lineRenderer.renderChildLimitReached(depth, skippedChildrenEntry);
	}

	private String renderMaxDepthReachEntry(Depth depth, MaxDepthReachEntry maxDepthReachEntry) {
		return lineRenderer.renderMaxDepthReached(depth, maxDepthReachEntry);
	}

}
