package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultEmojiMapping implements EmojiMapping {

	private final String directoryEmoji;
	private final String defaultFileEmoji;
	private final Map<String, String> exactFileNamesEmojis;
	private final Map<String, String> fileExtensionsEmojis;

	public DefaultEmojiMapping(
		String directoryEmoji, String defaultFileEmoji, Map<String, String> exactFileNamesEmojis, Map<String, String> fileExtensionsEmojis
	) {
		super();
		this.directoryEmoji = Objects.requireNonNull(directoryEmoji, "directoryEmoji is null");
		this.defaultFileEmoji = Objects.requireNonNull(defaultFileEmoji, "defaultFileEmoji is null");
		this.exactFileNamesEmojis = Objects.requireNonNull(exactFileNamesEmojis, "exactFileNamesEmojis is null");
		this.fileExtensionsEmojis = Objects.requireNonNull(fileExtensionsEmojis, "fileExtensionsEmojis is null");
	}

	@Override
	public @Nullable String getFileEmoji(Path path) {
		if (path.toFile().isDirectory()) {
			return directoryEmoji;
		}
		var fileName = path.getFileName().toString().toLowerCase();
		var emoji = exactFileNamesEmojis.get(fileName);
		if (emoji == null) {
			String extension = extractExtension(fileName);
			emoji = extension == null ? null : fileExtensionsEmojis.get(extension);
		}
		if (emoji == null) {
			emoji = defaultFileEmoji;
		}
		return emoji;
	}

	@Nullable
	private final String extractExtension(String fileName) {
		var dotIndex = fileName.lastIndexOf('.');
		return dotIndex < 0 ? null : fileName.substring(dotIndex + 1);
	}

	public static DefaultEmojiMappingBuilder builder() {
		return new DefaultEmojiMappingBuilder();
	}

}
