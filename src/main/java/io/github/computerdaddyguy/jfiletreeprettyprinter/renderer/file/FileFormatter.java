package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file;

import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.DirectoryEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.FileEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.MaxDepthReachEntry;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry.SkippedChildrenEntry;
import java.nio.file.Path;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface FileFormatter {

	String formatDirectoryBegin(DirectoryEntry dirEntry, List<Path> dirs);

	String formatFile(FileEntry fileEntry);

	String formatChildLimitReached(SkippedChildrenEntry skippedChildrenEntry);

	String formatMaxDepthReached(MaxDepthReachEntry maxDepthReachEntry);

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
