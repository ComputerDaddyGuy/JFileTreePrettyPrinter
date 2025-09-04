package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file;

import io.github.computerdaddyguy.jfiletreeprettyprinter.depth.Depth;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
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
	public String formatDirectoryBegin(List<Path> dirs) {
		return getFileEmojiPrefix(dirs.getLast()) + decorated.formatDirectoryBegin(dirs);
	}

	@Override
	public String formatDirectoryException(Path dir, IOException exc) {
		return getErrorEmojiPrefix(dir, exc) + decorated.formatDirectoryException(dir, exc);
	}

	@Override
	public String formatFile(Path file, BasicFileAttributes attrs) {
		return getFileEmojiPrefix(file) + decorated.formatFile(file, attrs);
	}

	@Override
	public String formatFileException(Path file, IOException exc) {
		return getErrorEmojiPrefix(file, exc) + decorated.formatFileException(file, exc);
	}

	@Override
	public String formatChildLimitReached(Collection<Path> notVisited) {
		return decorated.formatChildLimitReached(notVisited);
	}

	@Override
	public String formatMaxDepthReached(Depth depth) {
		return decorated.formatMaxDepthReached(depth);
	}

}
