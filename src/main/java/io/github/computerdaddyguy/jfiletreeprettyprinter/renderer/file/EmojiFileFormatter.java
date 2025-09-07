package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file;

import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryExceptionTreeEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileReadingAttributesExceptionEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
class EmojiFileFormatter implements FileFormatter {

	private final FileFormatter decorated;
	private final EmojiMapping emojiMapping;

	public EmojiFileFormatter(FileFormatter decorated, EmojiMapping emojiMapping) {
		this.decorated = Objects.requireNonNull(decorated, "decorated formatter is null");
		this.emojiMapping = Objects.requireNonNull(emojiMapping, "emoji mapping is null");
	}

	private String getFileEmojiPrefix(Path p) {
		var emoji = emojiMapping.getFileEmoji(p);
		return getEmojiPrefix(emoji);
	}

	private String getErrorEmojiPrefix(Path file, IOException exc) {
		var emoji = emojiMapping.getErrorEmoji(file, exc);
		return getEmojiPrefix(emoji);
	}

	private String getEmojiPrefix(String emoji) {
		if (emoji == null) {
			return "";
		}
		return emoji + " ";
	}

	@Override
	public String formatDirectoryBegin(DirectoryEntry dirEntry, List<Path> dirs) {
		return getFileEmojiPrefix(dirEntry.getDir()) + decorated.formatDirectoryBegin(dirEntry, dirs);
	}

	@Override
	public String formatDirectoryException(DirectoryExceptionTreeEntry dirExceptionEntry) {
		return getErrorEmojiPrefix(dirExceptionEntry.getDir(), dirExceptionEntry.getException())
			+ decorated.formatDirectoryException(dirExceptionEntry);
	}

	@Override
	public String formatFile(FileEntry fileEntry) {
		return getFileEmojiPrefix(fileEntry.getFile()) + decorated.formatFile(fileEntry);
	}

	@Override
	public String formatFileException(FileReadingAttributesExceptionEntry fileReadingAttrsException) {
		return getErrorEmojiPrefix(fileReadingAttrsException.getFile(), fileReadingAttrsException.getException())
			+ decorated.formatFileException(fileReadingAttrsException);
	}

	@Override
	public String formatChildLimitReached(SkippedChildrenEntry skippedChildrenEntry) {
		return decorated.formatChildLimitReached(skippedChildrenEntry);
	}

	@Override
	public String formatMaxDepthReached(MaxDepthReachEntry maxDepthReachEntry) {
		return decorated.formatMaxDepthReached(maxDepthReachEntry);
	}

}
