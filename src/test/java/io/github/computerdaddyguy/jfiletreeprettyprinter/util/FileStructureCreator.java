package io.github.computerdaddyguy.jfiletreeprettyprinter.util;

import java.io.IOException;
import java.nio.file.Path;

public class FileStructureCreator {

	private final FileStructureCreator parent;
	private final Path currentDir;

	private FileStructureCreator(FileStructureCreator parent, Path currentDir) {
		this.parent = parent;
		this.currentDir = currentDir;
	}

//	public static FileStructureCreator forRoot(Path root) {
//		return new FileStructureCreator(null, root);
//	}

	public static FileStructureCreator forTargetPath(Path root) {
		var newDir = root.resolve("targetPath");
		var created = newDir.toFile().mkdir();
		if (!created) {
			throw new IllegalStateException("Unable to create directory: " + newDir);
		}
		return new FileStructureCreator(null, newDir);
	}

	public FileStructureCreator createDirectories(String dirNamePrefix, int n) {
		for (int i = 0; i < n; i++) {
			createAndEnterDirectory(dirNamePrefix + (i + 1));
		}
		return this;
	}

	public FileStructureCreator createDirectory(String dirName) {
		return createAndEnterDirectory(dirName).up();
	}

	public FileStructureCreator createAndEnterDirectory(String dirName) {
		var newDir = currentDir.resolve(dirName);
		var created = newDir.toFile().mkdir();
		if (!created) {
			throw new IllegalStateException("Unable to create directory: " + newDir);
		}
		return new FileStructureCreator(this, newDir);
	}

	public FileStructureCreator createFiles(String fileNamePrefix, int n) {
		for (int i = 0; i < n; i++) {
			createFile(fileNamePrefix + (i + 1));
		}
		return this;
	}

	public FileStructureCreator createFile(String fileName) {
		var newFile = currentDir.resolve(fileName);
		try {
			var created = newFile.toFile().createNewFile();
			if (!created) {
				throw new IllegalStateException("Unable to create file: " + newFile);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to create file: " + newFile, e);
		}
		return this;
	}

	public FileStructureCreator up() {
		if (parent == null) {
			return this;
		}
		return parent;
	}

	public Path getPath() {
		return currentDir;
	}

}
