package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.limit;

import java.nio.file.Path;
import java.util.Set;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultChildrenLimitReachedFormatter implements ChildrenLimitReachedFormatter {

	@Override
	public String formatLimitReached(Set<Path> notVisited) {

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
