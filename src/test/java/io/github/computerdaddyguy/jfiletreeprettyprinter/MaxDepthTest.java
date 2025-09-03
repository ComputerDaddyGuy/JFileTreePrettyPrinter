package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MaxDepthTest {

	@TempDir
	private Path root;

	private FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
		.customizeOptions(options -> options.withMaxDepth(2))
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
	void withCompactDirectories() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
			.createAndEnterDirectory("level1")
				.createAndEnterDirectory("level2")
					.createAndEnterDirectory("level3")
						.createAndEnterDirectory("level4")
							.createFiles("file4#", 3)
						.up() // level4
					.up() // level3
				.up() // level2
			.up() // level1
			.getPath();
		// @formatter:on

		printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withMaxDepth(2).withCompactDirectories(true))
			.build();

		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/level1/level2/
			└─ ... (max depth reached)""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void nominal() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
			.createAndEnterDirectory("level1")
				.createFiles("file1#", 3)
				.createAndEnterDirectory("level2")
					.createFiles("file2#", 3)
					.createAndEnterDirectory("level3-1")
						.createFiles("file3-1#", 3)
						.createAndEnterDirectory("level4-1")
							.createFiles("file4-1#", 3)
						.up() // level4
					.up() // level3-1
					.createAndEnterDirectory("level3-2")
						.createAndEnterDirectory("level4-2")
							.createFiles("file4-2#", 3)
						.up() // level4-2
					.up() // level3-2
				.up() // level2
			.up() // level1
			.getPath();
		// @formatter:on

		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			└─ level1/
			   ├─ file1#1
			   ├─ file1#2
			   ├─ file1#3
			   └─ level2/
			      └─ ... (max depth reached)""";
		assertThat(result).isEqualTo(expected);
	}

}
