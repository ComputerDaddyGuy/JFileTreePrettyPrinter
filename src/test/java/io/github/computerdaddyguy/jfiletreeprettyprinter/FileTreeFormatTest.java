package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.RenderingOptions.TreeFormat;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class FileTreeFormatTest {

	@TempDir
	private Path root;

	private FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
		.customizeOptions(
			options -> options.withTreeFormat(TreeFormat.CLASSIC_ASCII)
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
	void dirWithFilesOnly() {
		var path = FileStructures.simpleDirectoryWithFiles(root, 3);
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			|-- file1
			|-- file2
			`-- file3""";
		assertThat(result).isEqualTo(expected);
	}

}
