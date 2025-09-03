package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface FileFormatter {

	String formatDirectoryBegin(List<Path> dirs, BasicFileAttributes attrs);

	String formatDirectoryException(Path dir, IOException exc);

	String formatFile(Path file, BasicFileAttributes attrs);

	String formatFileException(Path file, IOException exc);

	String formatChildLimitReached(Set<Path> notVisited);

	String formatMaxDepthReached(Set<Path> notVisited);

	static FileFormatter createDefault() {
		return new DefaultFileFormatter();
	}

	static FileFormatter wrapWithEmojis(FileFormatter decorated) {
		return wrapWithEmojis(decorated, EmojiMapping.createDefault());
	}

	static FileFormatter wrapWithEmojis(FileFormatter decorated, EmojiMapping emojiMapping) {
		return new EmojiFileFormatter(decorated, emojiMapping);
	}

}
