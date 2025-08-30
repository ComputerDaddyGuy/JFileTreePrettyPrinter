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

	public FileStructureCreator createDirectory(String dirName) {
		var newDir = currentDir.resolve(dirName);
		var created = newDir.toFile().mkdir();
		if (!created) {
			throw new IllegalStateException("Unable to create directory: " + newDir);
		}
		return new FileStructureCreator(this, newDir);
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

	public FileStructureCreator end() {
		if (parent == null) {
			return this;
		}
		return parent;
	}

	public Path getCurrentDir() {
		return currentDir;
	}

}
