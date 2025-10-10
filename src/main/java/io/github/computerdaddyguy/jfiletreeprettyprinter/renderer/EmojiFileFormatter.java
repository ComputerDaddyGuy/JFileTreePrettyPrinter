package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.emoji.EmojiMapping;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class EmojiFileFormatter implements FileFormatter {

	private final FileFormatter decorated;
	private final EmojiMapping emojiMapping;

	public EmojiFileFormatter(FileFormatter decorated, EmojiMapping emojiMapping) {
		this.decorated = Objects.requireNonNull(decorated, "decorated formatter is null");
		this.emojiMapping = Objects.requireNonNull(emojiMapping, "emoji mapping is null");
	}

	private String getFileEmojiPrefix(Path p) {
		var emoji = emojiMapping.getPathEmoji(p);
		return getEmojiPrefix(emoji);
	}

	private String getEmojiPrefix(@Nullable String emoji) {
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
	public String formatFile(FileEntry fileEntry) {
		return getFileEmojiPrefix(fileEntry.getFile()) + decorated.formatFile(fileEntry);
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
