package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface EmojiMapping {

	/**
	 * Get the emoji to display for the given file (i.e. the file type icon).
	 * 
	 * @return the emoji to use, or <code>null</code> if not emoji to use
	 */
	@Nullable
	String getPathEmoji(Path path);

	static EmojiMapping none() {
		return path -> null;
	}

	static EmojiMapping createDefault() {
		return DefaultEmojiMappingBuilder.newDefaultInstance().build();
	}

	static EmojiMappingBuilder builderFromDefault() {
		return DefaultEmojiMappingBuilder.newDefaultInstance();
	}

	static EmojiMappingBuilder builderFromBlank() {
		return DefaultEmojiMappingBuilder.newBlankInstance();
	}

}
