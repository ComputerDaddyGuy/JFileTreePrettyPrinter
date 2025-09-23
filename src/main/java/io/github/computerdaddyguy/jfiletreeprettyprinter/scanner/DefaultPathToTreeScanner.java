package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PathPredicates;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryExceptionTreeEntry.DirectoryListingExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileReadingAttributesExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.io.IOException;
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
	private TreeEntry handle(int depth, Path fileOrDir, @Nullable Predicate<Path> filter) {
		return fileOrDir.toFile().isDirectory()
			? handleDirectory(depth, fileOrDir, filter)
			: handleFile(fileOrDir);
	}

	@Nullable
	private TreeEntry handleDirectory(int depth, Path dir, @Nullable Predicate<Path> filter) {

		if (depth >= options.getMaxDepth()) {
			var maxDepthEntry = new MaxDepthReachEntry(depth);
			return new DirectoryEntry(dir, List.of(maxDepthEntry));
		}

		List<TreeEntry> childEntries;

		try (var childrenStream = Files.newDirectoryStream(dir)) {
			var it = directoryStreamToIterator(childrenStream, filter);
			childEntries = handleDirectoryChildren(depth, dir, filter, it);
		} catch (IOException e) {
			childEntries = List.of(new DirectoryListingExceptionEntry(dir, e));
		}

		// Filter is active and no children match
		if (depth > 0 && filter != null && childEntries.isEmpty() && !filter.test(dir)) {
			return null; // Do no show this directory at all
		}

		return new DirectoryEntry(dir, childEntries);
	}

	private List<TreeEntry> handleDirectoryChildren(int depth, Path dir, Predicate<Path> filter, Iterator<Path> pathIterator) {

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
			childEntries.addAll(handleLeftOverChildren(depth, filter, pathIterator));
		}

		return childEntries;
	}

	private List<TreeEntry> handleLeftOverChildren(int depth, Predicate<Path> filter, Iterator<Path> pathIterator) {
		var childEntries = new ArrayList<TreeEntry>();

		if (filter == null) {
			var skippedChildren = new ArrayList<Path>();
			pathIterator.forEachRemaining(skippedChildren::add);
			var childrenSkippedEntry = new SkippedChildrenEntry(skippedChildren);
			childEntries.add(childrenSkippedEntry);
		} else {
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
		}

		return childEntries;
	}

	private Iterator<Path> directoryStreamToIterator(DirectoryStream<Path> childrenStream, @Nullable Predicate<Path> filter) {
		var stream = StreamSupport.stream(childrenStream.spliterator(), false);
		if (filter != null) {
			var recursiveFilter = PathPredicates.isDirectory().or(filter);
			stream = stream.filter(recursiveFilter);
		}
		return stream
			.sorted(options.pathComparator())
			.iterator();
	}

	@Nullable
	private TreeEntry handleFile(Path file) {
		try {
			var attrs = Files.readAttributes(file, BasicFileAttributes.class);
			return new FileEntry(file, attrs);
		} catch (IOException e) {
			return new FileReadingAttributesExceptionEntry(file, e);
		}
	}

}
