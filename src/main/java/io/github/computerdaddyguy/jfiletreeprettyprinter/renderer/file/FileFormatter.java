package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file;

import io.github.computerdaddyguy.jfiletreeprettyprinter.depth.Depth;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface FileFormatter {

	String formatDirectoryBegin(List<Path> dirs);

	String formatDirectoryException(Path dir, IOException exc);

	String formatFile(Path file, BasicFileAttributes attrs);

	String formatFileException(Path file, IOException exc);

	String formatChildLimitReached(Collection<Path> notVisited);

	String formatMaxDepthReached(Depth depth);

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
