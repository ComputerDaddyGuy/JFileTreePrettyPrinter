package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileTreePrettyPrinterBuilderTest {

	@TempDir
	private Path root;

	@Test
	void withOptions_overrides() {
		var path = FileStructures.simpleDirectoryWithFilesAndFolders(root, 3, 3);

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(1))
			.withOptions(PrettyPrintOptions.createDefault().withChildLimit(2)) // this line overrides previous one
			.build();

		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ file1
			├─ file2
			└─ ... (1 file and 3 directories skipped)""";
		assertThat(result).isEqualTo(expected);
	}

}
