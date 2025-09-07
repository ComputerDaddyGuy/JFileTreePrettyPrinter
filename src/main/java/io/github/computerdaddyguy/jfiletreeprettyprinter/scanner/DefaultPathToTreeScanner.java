package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryExceptionTreeEntry.DirectoryListingExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileReadingAttributesExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultPathToTreeScanner implements PathToTreeScanner {

	private final ScanningOptions options;

	public DefaultPathToTreeScanner(ScanningOptions options) {
		this.options = Objects.requireNonNull(options, "options is null");
	}

	@Override
	public TreeEntry scan(Path fileOrDir) {
		return handle(0, fileOrDir);
	}

	private TreeEntry handle(int depth, Path fileOrDir) {
		return fileOrDir.toFile().isDirectory()
			? handleDirectory(depth, fileOrDir)
			: handleFile(fileOrDir);
	}

	private TreeEntry handleDirectory(int depth, Path dir) {

		if (depth >= options.getMaxDepth()) {
			var maxDepthEntry = new MaxDepthReachEntry(depth);
			return new DirectoryEntry(dir, List.of(maxDepthEntry));
		}

		var childrenEntries = new ArrayList<TreeEntry>();
		int maxChildrenEntries = options.getChildrenLimitFunction().apply(dir);

		try (var childrenStream = Files.newDirectoryStream(dir)) {
			var it = childrenStream.iterator();
			var childCount = 0;
			while (it.hasNext()) {
				childCount++;
				if (maxChildrenEntries >= 0 && childCount > maxChildrenEntries) {
					break;
				}
				var child = it.next();
				var childEntry = handle(depth + 1, child);
				childrenEntries.add(childEntry);
			}

			if (it.hasNext()) { // Loop has early exit?
				var skippedChildren = new ArrayList<Path>();
				it.forEachRemaining(skippedChildren::add);
				var childrenSkippedEntry = new SkippedChildrenEntry(skippedChildren);
				childrenEntries.add(childrenSkippedEntry);
			}

		} catch (IOException e) {
			var exceptionEntry = new DirectoryListingExceptionEntry(dir, e);
			childrenEntries.add(exceptionEntry);
		}

		return new DirectoryEntry(dir, childrenEntries);

	}

	private TreeEntry handleFile(Path file) {

		BasicFileAttributes attrs;
		try {
			attrs = Files.readAttributes(file, BasicFileAttributes.class);
		} catch (IOException e) {
			return new FileReadingAttributesExceptionEntry(file, e);
		}

		return new FileEntry(file, attrs);
	}

}
