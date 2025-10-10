package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.nio.file.Path;
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
	public String formatChildLimitReached(SkippedChildrenEntry skippedChildrenEntry) {
		return "...";
	}

	@Override
	public String formatMaxDepthReached(MaxDepthReachEntry maxDepthReachEntry) {
		return "...";
	}

}
