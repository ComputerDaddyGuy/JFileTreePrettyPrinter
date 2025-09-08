package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChildrenLimitDynamicTest {

	@TempDir
	private Path root;

	private FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
		.customizeOptions(
			options -> options.withChildrenLimitFunction(
				p -> p.getFileName().toString().equals("limit_1") ? 1
					: p.getFileName().toString().equals("limit_3") ? 3
						: -1
			)
		)
		.build();

	@Test
	void emptyDir() {
		var path = FileStructures.emptyDirectory(root);
		var result = printer.prettyPrint(path);
		var expected = "targetPath/";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void not_limited_dir() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createAndEnterDirectory("simpleDir")
					.createFiles("file", 5)
				.up()
			.getPath()
			;
		// @formatter:on

		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			└─ simpleDir/
			   ├─ file1
			   ├─ file2
			   ├─ file3
			   ├─ file4
			   └─ file5""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void limited_dir_1() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createAndEnterDirectory("limit_1")
					.createFiles("file", 5)
				.up()
			.getPath()
			;
		// @formatter:on

		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			└─ limit_1/
			   ├─ file1
			   └─ ... (4 files skipped)""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void limited_dir_3() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createAndEnterDirectory("limit_3")
					.createFiles("file", 5)
				.up()
			.getPath()
			;
		// @formatter:on

		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			└─ limit_3/
			   ├─ file1
			   ├─ file2
			   ├─ file3
			   └─ ... (2 files skipped)""";
		assertThat(result).isEqualTo(expected);
	}

}
