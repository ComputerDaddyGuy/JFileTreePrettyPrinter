package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file;

import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface EmojiMapping {

	/**
	 * Get the emoji to display for the given file (i.e. the file type icon).
	 */
	@Nullable
	String getFileEmoji(Path file);

	static EmojiMapping createDefault() {
		return DefaultEmojiMapping.builder().build();
	}

}
