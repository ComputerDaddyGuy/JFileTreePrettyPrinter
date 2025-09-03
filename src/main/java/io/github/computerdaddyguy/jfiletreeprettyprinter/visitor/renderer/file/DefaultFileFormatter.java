package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultFileFormatter implements FileFormatter {

	@Override
	public String formatDirectoryBegin(List<Path> dirs, BasicFileAttributes attrs) {
		return dirs.stream()
			.map(dir -> dir.getFileName().toString() + "/")
			.collect(Collectors.joining());
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
