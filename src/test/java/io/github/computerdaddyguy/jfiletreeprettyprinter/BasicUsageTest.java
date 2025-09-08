package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BasicUsageTest {

	@TempDir
	private Path root;

	private FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
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
		var path = FileStructures.simpleDirectoryWithFiles(root, 3);
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
			targetPath/
			└─ dir1/""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void complex1() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createAndEnterDirectory("level_1")
					.createAndEnterDirectory("level_1_1")
						.createFiles("file", 2)
					.up()
				.up()
				.createAndEnterDirectory("level_2")
					.createAndEnterDirectory("level_2_1")
						.createFiles("file", 2)
					.up()
				.up()
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ level_1/
			│  └─ level_1_1/
			│     ├─ file1
			│     └─ file2
			└─ level_2/
			   └─ level_2_1/
			      ├─ file1
			      └─ file2""";
		assertThat(result).isEqualTo(expected);
	}

}
