package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.Set;
import org.jspecify.annotations.NullMarked;

@NullMarked
class EmojiFileFormatter implements FileFormatter {

	private final FileFormatter decorated;
	private final EmojiMapping emojiMapping;

	public EmojiFileFormatter(FileFormatter decorated, EmojiMapping emojiMapping) {
		this.decorated = Objects.requireNonNull(decorated, "decorated formatter is null");
		this.emojiMapping = Objects.requireNonNull(emojiMapping, "emoji mapping is null");
	}

	private String getFileEmojiPrefix(Path p, BasicFileAttributes attrs) {
		var emoji = emojiMapping.getFileEmoji(p, attrs);
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
	public String formatDirectoryBegin(Path dir, BasicFileAttributes attrs) {
		return getFileEmojiPrefix(dir, attrs) + decorated.formatDirectoryBegin(dir, attrs);
	}

	@Override
	public String formatDirectoryException(Path dir, IOException exc) {
		return getErrorEmojiPrefix(dir, exc) + decorated.formatDirectoryException(dir, exc);
	}

	@Override
	public String formatFile(Path file, BasicFileAttributes attrs) {
		return getFileEmojiPrefix(file, attrs) + decorated.formatFile(file, attrs);
	}

	@Override
	public String formatFileException(Path file, IOException exc) {
		return getErrorEmojiPrefix(file, exc) + decorated.formatFileException(file, exc);
	}

	@Override
	public String formatChildLimitReached(Set<Path> notVisited) {
		return decorated.formatChildLimitReached(notVisited);
	}

}
