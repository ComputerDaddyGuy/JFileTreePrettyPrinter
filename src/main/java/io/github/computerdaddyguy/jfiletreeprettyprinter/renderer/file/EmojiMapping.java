package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file;

import java.io.IOException;
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

	/**
	 * Get the emoji to display for the given file, in case of error.
	 */
	@Nullable
	String getErrorEmoji(Path file, IOException exc);

	static EmojiMapping createDefault() {
		return DefaultEmojiMapping.builder().build();
	}

}
