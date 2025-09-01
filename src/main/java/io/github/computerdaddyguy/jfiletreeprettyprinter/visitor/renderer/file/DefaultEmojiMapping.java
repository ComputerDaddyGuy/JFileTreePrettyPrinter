package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
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
	private final String errorEmoji;

	public DefaultEmojiMapping(
		String directoryEmoji, String defaultFileEmoji, Map<String, String> exactFileNamesEmojis, Map<String, String> fileExtensionsEmojis, String errorEmoji
	) {
		super();
		this.directoryEmoji = Objects.requireNonNull(directoryEmoji, "directoryEmoji is null");
		this.defaultFileEmoji = Objects.requireNonNull(defaultFileEmoji, "defaultFileEmoji is null");
		this.exactFileNamesEmojis = Objects.requireNonNull(exactFileNamesEmojis, "exactFileNamesEmojis is null");
		this.fileExtensionsEmojis = Objects.requireNonNull(fileExtensionsEmojis, "fileExtensionsEmojis is null");
		this.errorEmoji = Objects.requireNonNull(errorEmoji, "errorEmoji is null");
	}

	@Override
	public @Nullable String getFileEmoji(Path file, BasicFileAttributes attrs) {
		if (attrs.isDirectory()) {
			return directoryEmoji;
		}
		var fileName = file.getFileName().toString().toLowerCase();
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

	private final String extractExtension(String fileName) {
		var dotIndex = fileName.lastIndexOf('.');
		return dotIndex < 0 ? null : fileName.substring(dotIndex + 1);
	}

	@Override
	public @Nullable String getErrorEmoji(Path file, IOException exc) {
		return errorEmoji;
	}

	public static DefaultEmojiMappingBuilder builder() {
		return new DefaultEmojiMappingBuilder();
	}

}
