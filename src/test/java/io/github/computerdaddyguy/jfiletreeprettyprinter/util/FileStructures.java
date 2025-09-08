package io.github.computerdaddyguy.jfiletreeprettyprinter.util;

import java.nio.file.Path;

public final class FileStructures {

	private FileStructures() {
		// Helper class
	}

	public static Path emptyDirectory(Path root) {
		// @formatter:off
		return FileStructureCreator
			.forTargetPath(root)
			.getPath()
			;
		// @formatter:on
	}

	public static Path simpleDirectoryWithFiles(Path root, int fileCount) {
		// @formatter:off
		return FileStructureCreator
			.forTargetPath(root)
				.createFiles("file", fileCount)
			.getPath()
			;
		// @formatter:on
	}

	public static Path simpleDirectoryWithFilesAndFolders(Path root, int fileCount, int dirCount) {
		// @formatter:off
		return FileStructureCreator
			.forTargetPath(root)
				.createFiles("file", fileCount)
				.createDirectories("folder", dirCount)
			.getPath()
			;
		// @formatter:on
	}

}
