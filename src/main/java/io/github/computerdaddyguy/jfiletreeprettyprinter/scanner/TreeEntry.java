package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public sealed interface TreeEntry {

	final class DirectoryEntry implements TreeEntry {

		final Path dir;
		final List<TreeEntry> entries;

		public DirectoryEntry(Path dir, List<TreeEntry> entries) {
			this.dir = Objects.requireNonNull(dir, "dir is null");
			this.entries = Objects.requireNonNull(entries, "entries is null");
		}

		@Override
		public String toString() {
			return "DirectoryEntry[dir: " + dir.getFileName() + ", entries: " + entries + "]";
		}

		public Path getDir() {
			return dir;
		}

		public List<TreeEntry> getEntries() {
			return entries;
		}

	}

	public sealed interface DirectoryExceptionTreeEntry extends TreeEntry {

		Path getDir();

		IOException getException();

		final class DirectoryReadingAttributesExceptionEntry implements DirectoryExceptionTreeEntry {

			final Path dir;
			final IOException exception;

			public DirectoryReadingAttributesExceptionEntry(Path dir, IOException exception) {
				this.dir = Objects.requireNonNull(dir, "dir is null");
				this.exception = Objects.requireNonNull(exception, "exception is null");
			}

			@Override
			public String toString() {
				return "DirectoryReadingAttributesExceptionEntry[dir: " + dir.getFileName() + ", exception: " + exception + "]";
			}

			@Override
			public Path getDir() {
				return dir;
			}

			@Override
			public IOException getException() {
				return exception;
			}

		}

		final class DirectoryListingExceptionEntry implements DirectoryExceptionTreeEntry {

			final Path dir;
			final IOException exception;

			public DirectoryListingExceptionEntry(Path dir, IOException exception) {
				this.dir = Objects.requireNonNull(dir, "dir is null");
				this.exception = Objects.requireNonNull(exception, "exception is null");
			}

			@Override
			public String toString() {
				return "DirectoryListingExceptionEntry[exception: " + exception + "]";
			}

			@Override
			public Path getDir() {
				return dir;
			}

			@Override
			public IOException getException() {
				return exception;
			}

		}

	}

	final class FileEntry implements TreeEntry {

		final Path file;
		final BasicFileAttributes attrs;

		public FileEntry(Path file, BasicFileAttributes attrs) {
			this.file = Objects.requireNonNull(file, "file is null");
			this.attrs = Objects.requireNonNull(attrs, "attrs is null");
		}

		@Override
		public String toString() {
			return "FileEntry[file: " + file.getFileName() + "]";
		}

		public Path getFile() {
			return file;
		}

		public BasicFileAttributes getAttrs() {
			return attrs;
		}

	}

	final class FileReadingAttributesExceptionEntry implements TreeEntry {

		final Path file;
		final IOException exception;

		public FileReadingAttributesExceptionEntry(Path file, IOException exception) {
			this.file = Objects.requireNonNull(file, "file is null");
			this.exception = Objects.requireNonNull(exception, "exception is null");
		}

		@Override
		public String toString() {
			return "FileReadingAttributesExceptionEntry[file: " + file.getFileName() + ", exception: " + exception + "]";
		}

		public Path getFile() {
			return file;
		}

		public IOException getException() {
			return exception;
		}

	}

	final class SkippedChildrenEntry implements TreeEntry {

		final Collection<Path> skippedChildren;

		public SkippedChildrenEntry(List<Path> skippedChildren) {
			this.skippedChildren = Objects.requireNonNull(skippedChildren, "skippedChildren is null");
		}

		@Override
		public String toString() {
			return "SkippedChildrenEntry[skippedChildren: " + skippedChildren.stream().map(Path::getFileName).toList() + "]";
		}

		public Collection<Path> getSkippedChildren() {
			return skippedChildren;
		}

	}

	final class MaxDepthReachEntry implements TreeEntry {

		final int depth;

		public MaxDepthReachEntry(int depth) {
			if (depth < 0) {
				throw new IllegalArgumentException("depth reached cannot be negative");
			}
			this.depth = depth;
		}

		@Override
		public String toString() {
			return "MaxDepthReachEntry[depth: " + depth + "]";
		}

		public int getDepth() {
			return depth;
		}

	}

}
