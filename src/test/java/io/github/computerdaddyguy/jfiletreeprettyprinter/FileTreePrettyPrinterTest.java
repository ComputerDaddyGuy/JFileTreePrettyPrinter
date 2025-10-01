package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileTreePrettyPrinterTest {

	@TempDir
	private Path root;

	@Test
	void prettyPrint_by_path_and_string_are_same() {
		var path = FileStructures.simpleDirectoryWithFilesAndFolders(root, 3, 3);

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.createDefault();

		assertThat(printer.prettyPrint(path)).isEqualTo(printer.prettyPrint(path.toString()));
	}

}
