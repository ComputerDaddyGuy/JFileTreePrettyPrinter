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

	public EmojiFileFormatter(FileFormatter decorated) {
		this.decorated = Objects.requireNonNull(decorated, "decorated formatter is null");
	}

	@Override
	public String formatDirectoryBegin(Path dir, BasicFileAttributes attrs) {
		return "📂 " + decorated.formatDirectoryBegin(dir, attrs);
	}

	@Override
	public String formatDirectoryException(Path dir, IOException exc) {
		return "❌ " + decorated.formatDirectoryException(dir, exc);
	}

	@Override
	public String formatFile(Path file, BasicFileAttributes attrs) {
		return decorated.formatFile(file, attrs);
	}

	@Override
	public String formatFileException(Path file, IOException exc) {
		return "❌ " + decorated.formatFileException(file, exc);
	}

	@Override
	public String formatChildLimitReached(Set<Path> notVisited) {
		return decorated.formatChildLimitReached(notVisited);
	}

}
