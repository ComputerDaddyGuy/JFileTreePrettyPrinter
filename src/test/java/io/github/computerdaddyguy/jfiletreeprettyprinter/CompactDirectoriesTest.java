package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CompactDirectoriesTest {

	@TempDir
	private Path root;

	private FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
		.customizeOptions(options -> options.withCompactDirectories(true))
		.build();

	@Test
	void emptyDir() {
		var path = FileStructures.emptyDirectory(root);
		var result = printer.prettyPrint(path);
		var expected = "targetPath/";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWithFilesOnly() {
		var path = FileStructures.treeFileDirectory(root);
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ file1
			├─ file2
			└─ file3""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWithOneDir() {
		var path = FileStructureCreator.forTargetPath(root)
			.createDirectory("dir1")
			.getPath();
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/dir1/""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWithOneDirAndOneFile() {
		var path = FileStructureCreator.forTargetPath(root)
			.createDirectory("dir1")
			.createFile("file1")
			.getPath();
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ dir1/
			└─ file1""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWithTwoDirs() {
		var path = FileStructureCreator
			.forTargetPath(root)
			.createDirectory("dir1")
			.createDirectory("dir2")
			.up()
			.getPath();
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ dir1/
			└─ dir2/""";
		assertThat(result).isEqualTo(expected);
	}

}
