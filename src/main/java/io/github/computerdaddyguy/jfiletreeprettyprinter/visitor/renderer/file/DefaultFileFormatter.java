package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultFileFormatter implements FileFormatter {

	@Override
	public String formatDirectoryBegin(Path dir, BasicFileAttributes attrs) {
		return dir.getFileName().toString() + "/";
	}

	@Override
	public String formatDirectoryException(Path dir, IOException exc) {
		return dir.getFileName().toString() + "/: " + exc.getMessage();
	}

	@Override
	public String formatFile(Path file, BasicFileAttributes attrs) {
		var fileNameFormatted = file.getFileName().toString();
		if (attrs.isSymbolicLink()) {
			fileNameFormatted += "*";
		} else if (attrs.isOther()) {
			fileNameFormatted += "?";
		}
		return fileNameFormatted;
	}

	@Override
	public String formatFileException(Path file, IOException exc) {
		return file.getFileName().toString() + ": " + exc.getMessage();
	}

	@Override
	public String formatChildLimitReached(Set<Path> notVisited) {

		var dirCount = countDirs(notVisited);
		var fileCount = countFiles(notVisited, dirCount);

		var dirText = dirText(dirCount);
		var fileText = fileText(fileCount);

		return "... (" + fileText + (!fileText.isEmpty() && !dirText.isEmpty() ? " and " : "") + dirText + " skipped)";
	}

	private long countDirs(Set<Path> notVisited) {
		return notVisited.stream().filter(path -> path.toFile().isDirectory()).count();
	}

	private String dirText(long dirCount) {
		if (dirCount == 0) {
			return "";
		}
		if (dirCount == 1) {
			return "1 directory";
		}
		return dirCount + " directories";
	}

	private long countFiles(Set<Path> notVisited, long dirCount) {
		return notVisited.size() - dirCount;
	}

	private String fileText(long fileCount) {
		if (fileCount == 0) {
			return "";
		}
		if (fileCount == 1) {
			return "1 file";
		}
		return fileCount + " files";
	}

}
