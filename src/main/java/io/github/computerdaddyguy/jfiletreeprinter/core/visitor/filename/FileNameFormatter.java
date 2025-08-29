package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.filename;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface FileNameFormatter {

	@Nullable
	String formatDirectoryBegin(Path dir, BasicFileAttributes attrs);

	@Nullable
	String formatDirectoryException(Path dir, IOException exc);

	@Nullable
	String formatFile(Path file, BasicFileAttributes attrs);

	@Nullable
	String formatFileException(Path file, IOException exc);

	static FileNameFormatter createDefault() {
		return new DefaultFileNameFormatter();
	}

}
