package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file;

import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryExceptionTreeEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileReadingAttributesExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultFileFormatter implements FileFormatter {

	@Override
	public String formatDirectoryBegin(DirectoryEntry dirEntry, List<Path> dirs) {
		return dirs.stream()
			.map(dir -> dir.getFileName().toString() + "/")
			.collect(Collectors.joining());
	}

	@Override
	public String formatDirectoryException(DirectoryExceptionTreeEntry dirExceptionEntry) {
		return dirExceptionEntry.getDir().getFileName().toString() + "/: "
			+ dirExceptionEntry.getException().getMessage();
	}

	@Override
	public String formatFile(FileEntry fileEntry) {
		var fileNameFormatted = fileEntry.getFile().getFileName().toString();
		if (fileEntry.getAttrs().isSymbolicLink()) {
			fileNameFormatted += "*";
		} else if (fileEntry.getAttrs().isOther()) {
			fileNameFormatted += "?";
		}
		return fileNameFormatted;
	}

	@Override
	public String formatFileException(FileReadingAttributesExceptionEntry fileReadingAttrsException) {
		return fileReadingAttrsException.getFile().getFileName().toString() + ": " + fileReadingAttrsException.getException().getMessage();
	}

	@Override
	public String formatChildLimitReached(SkippedChildrenEntry skippedChildrenEntry) {
		return "... (" + childrenAsString(skippedChildrenEntry.getSkippedChildren()) + " skipped)";
	}

	@Override
	public String formatMaxDepthReached(MaxDepthReachEntry maxDepthReachEntry) {
		return "... (max depth reached)";
	}

	private String childrenAsString(Collection<Path> notVisited) {

		var dirCount = countDirs(notVisited);
		var fileCount = countFiles(notVisited, dirCount);

		var dirText = dirText(dirCount);
		var fileText = fileText(fileCount);

		return fileText + (!fileText.isEmpty() && !dirText.isEmpty() ? " and " : "") + dirText;
	}

	private long countDirs(Collection<Path> notVisited) {
		return notVisited.stream().filter(path -> path.toFile().isDirectory()).count();
	}

	private String dirText(long dirCount) {
		if (dirCount == 0) {
			return "";
		}
		if (dirCount == 1) {
			return "1 directory";
		}
		return dirCount + " directories";
	}

	private long countFiles(Collection<Path> notVisited, long dirCount) {
		return notVisited.size() - dirCount;
	}

	private String fileText(long fileCount) {
		if (fileCount == 0) {
			return "";
		}
		if (fileCount == 1) {
			return "1 file";
		}
		return fileCount + " files";
	}

}
