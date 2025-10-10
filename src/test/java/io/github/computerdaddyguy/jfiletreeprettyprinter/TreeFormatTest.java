package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.TreeFormats;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TreeFormatTest {

	@TempDir
	private Path root;

	@Test
	void emptyDir() {
		var path = FileStructures.emptyDirectory(root);

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(
				options -> options.withTreeFormat(TreeFormats.CLASSIC_ASCII)
			)
			.build();

		var result = printer.prettyPrint(path);
		var expected = "targetPath/";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWithFilesOnly() {
		var path = FileStructures.simpleDirectoryWithFiles(root, 3);

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(
				options -> options.withTreeFormat(TreeFormats.CLASSIC_ASCII)
			)
			.build();

		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			|-- file1
			|-- file2
			`-- file3""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void customFormat() {

		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
			.createAndEnterDirectory("dirA")
				.createAndEnterDirectory("dirB")
					.createAndEnterDirectory("dirC")
						.createFiles("file", 3)
					.up()
				.up()
			.up()
			.createDirectory("dirX")
			.getPath();
		// @formatter:on

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(
				options -> options.withTreeFormat(TreeFormats.create("[", "^", "-", "."))
			)
			.build();

		var result = printer.prettyPrint(path);

		var expected = """
			targetPath/
			[ dirA/
			- ^ dirB/
			- . ^ dirC/
			- . . [ file1
			- . . [ file2
			- . . ^ file3
			^ dirX/""";
		assertThat(result).isEqualTo(expected);
	}

}
