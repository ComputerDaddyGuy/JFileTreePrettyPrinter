package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultPathToTreeScanner implements PathToTreeScanner {

	private final ScanningOptions options;

	public DefaultPathToTreeScanner(ScanningOptions options) {
		this.options = Objects.requireNonNull(options, "options is null");
	}

	@Override
	public TreeEntry scan(Path fileOrDir) {
		return handle(0, fileOrDir, options.pathFilter());
	}

	@Nullable
	private TreeEntry handle(int depth, Path fileOrDir, Predicate<Path> filter) {
		return fileOrDir.toFile().isDirectory()
			? handleDirectory(depth, fileOrDir, filter)
			: handleFile(fileOrDir);
	}

	@Nullable
	private TreeEntry handleDirectory(int depth, Path dir, Predicate<Path> filter) {

		if (depth >= options.getMaxDepth()) {
			var maxDepthEntry = new MaxDepthReachEntry(depth);
			return new DirectoryEntry(dir, List.of(maxDepthEntry));
		}

		List<TreeEntry> childEntries;

		try (var childrenStream = Files.newDirectoryStream(dir)) {
			var it = directoryStreamToIterator(childrenStream, filter);
			childEntries = handleDirectoryChildren(depth, dir, it, filter);
		} catch (IOException e) {
			throw new UncheckedIOException("Unable to list files for directory: " + dir, e);
		}

		return new DirectoryEntry(dir, childEntries);
	}

	private List<TreeEntry> handleDirectoryChildren(int depth, Path dir, Iterator<Path> pathIterator, Predicate<Path> filter) {

		var childEntries = new ArrayList<TreeEntry>();
		int maxChildEntries = options.getChildLimit().applyAsInt(dir);
		var hasChildLimitation = maxChildEntries >= 0;

		var childCount = 0;
		while (pathIterator.hasNext()) {
			childCount++;
			if (hasChildLimitation && childCount > maxChildEntries) {
				break;
			}
			var child = pathIterator.next();
			var childEntry = handle(depth + 1, child, filter);
			if (childEntry == null) {
				childCount--; // The child did not pass the filter, so it doesn't count
			} else {
				childEntries.add(childEntry);
			}
		}

		// Loop has early exit?
		if (pathIterator.hasNext()) {
			childEntries.addAll(handleLeftOverChildren(depth, pathIterator, filter));
		}

		return childEntries;
	}

	private List<TreeEntry> handleLeftOverChildren(int depth, Iterator<Path> pathIterator, Predicate<Path> filter) {
		var childEntries = new ArrayList<TreeEntry>();

		var skippedChildren = new ArrayList<Path>();
		while (pathIterator.hasNext()) {
			var child = pathIterator.next();
			var childEntry = handle(depth + 1, child, filter);
			if (childEntry != null) { // Is null if no children file is retained by filter
				skippedChildren.add(child);
			}
		}
		if (!skippedChildren.isEmpty()) {
			var childrenSkippedEntry = new SkippedChildrenEntry(skippedChildren);
			childEntries.add(childrenSkippedEntry);
		}

		return childEntries;
	}

	private Iterator<Path> directoryStreamToIterator(DirectoryStream<Path> childrenStream, Predicate<Path> filter) {
		return StreamSupport
			.stream(childrenStream.spliterator(), false)
			.filter(filter)
			.sorted(options.pathComparator())
			.iterator();
	}

	@Nullable
	private TreeEntry handleFile(Path file) {
		try {
			var attrs = Files.readAttributes(file, BasicFileAttributes.class);
			return new FileEntry(file, attrs);
		} catch (IOException e) {
			throw new UncheckedIOException("Unable to read file attributes: " + file, e);
		}
	}

}
