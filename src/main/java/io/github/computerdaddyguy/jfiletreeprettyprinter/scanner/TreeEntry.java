package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
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

	final class SkippedChildrenEntry implements TreeEntry {

		final Path dir;

		public SkippedChildrenEntry(Path dir) {
			this.dir = Objects.requireNonNull(dir, "dir is null");
		}

		@Override
		public String toString() {
			return "SkippedChildrenEntry[dir: " + dir.getFileName() + "]";
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
