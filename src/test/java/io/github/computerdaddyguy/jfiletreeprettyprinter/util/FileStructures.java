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
			.up()
			.getPath()
			;
		// @formatter:on
	}

	public static Path oneFileDirectory(Path root) {
		// @formatter:off
		return FileStructureCreator
			.forTargetPath(root)
				.createFiles("file", 1)
			.up()
			.getPath()
			;
		// @formatter:on
	}

	public static Path twoFileDirectory(Path root) {
		// @formatter:off
		return FileStructureCreator
			.forTargetPath(root)
				.createFiles("file", 2)
			.up()
			.getPath()
			;
		// @formatter:on
	}

	public static Path treeFileDirectory(Path root) {
		// @formatter:off
		return FileStructureCreator
			.forTargetPath(root)
				.createFiles("file", 3)
			.up()
			.getPath()
			;
		// @formatter:on
	}

}
