package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.filename;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultFileNameFormatter implements FileNameFormatter {

	public DefaultFileNameFormatter() {
	}

	@Override
	public String formatDirectoryBegin(Path dir, BasicFileAttributes attrs) {
		return "📂 " + dir.getFileName().toString() + "/";
	}

	@Override
	public String formatDirectoryException(Path dir, IOException exc) {
		return "❌ " + dir.getFileName().toString() + "/: " + exc.getMessage();
	}

	@Override
	public String formatFile(Path file, BasicFileAttributes attrs) {
		if (attrs.isSymbolicLink()) {
			return file.getFileName().toString() + "*";
		} else if (attrs.isOther()) {
			return file.getFileName().toString() + "?";
		}
		return file.getFileName().toString();
	}

	@Override
	public String formatFileException(Path file, IOException exc) {
		return "❌ " + file.getFileName().toString() + ": " + exc.getMessage();
	}

}
