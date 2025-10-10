package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.emoji;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PathMatchers;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultEmojiMapping implements EmojiMapping {

	private final Function<Path, String> dirEmojis;
	private final String defaultDirEmoji;

	private final Function<Path, String> fileEmojis;
	private final String defaultFileEmoji;

	public DefaultEmojiMapping(String defaultDirEmoji, Function<Path, String> dirEmojis, String defaultFileEmoji, Function<Path, String> fileEmojis) {
		super();
		this.dirEmojis = Objects.requireNonNull(dirEmojis, "dirEmojis is null");
		this.defaultDirEmoji = Objects.requireNonNull(defaultDirEmoji, "defaultDirEmoji is null");
		this.fileEmojis = Objects.requireNonNull(fileEmojis, "fileEmojis is null");
		this.defaultFileEmoji = Objects.requireNonNull(defaultFileEmoji, "defaultFileEmoji is null");
	}

	@Override
	public @Nullable String getPathEmoji(Path path) {
		return PathMatchers.isDirectory().matches(path)
			? Optional.ofNullable(dirEmojis.apply(path)).orElse(defaultDirEmoji)
			: Optional.ofNullable(fileEmojis.apply(path)).orElse(defaultFileEmoji);
	}

}
