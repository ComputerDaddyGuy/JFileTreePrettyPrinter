package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SortingTest {

	@TempDir
	private Path root;

	private Path buildDefaultPath() {
		// @formatter:off
		return FileStructureCreator.forTargetPath(root)
			.createAndEnterDirectory("dir1")
				.createFiles("files_1_", 3)
				.up()
			.createAndEnterDirectory("dir2")
				.createFiles("files_2_", 3)
				.up()
			.createAndEnterDirectory("dir3")
				.createFiles("files_3_", 3)
				.up()
			.getPath();
		// @formatter:on
	}

	@Test
	void defaultOrderIsAlphabetical() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.createDefault();

		var result = printer.prettyPrint(buildDefaultPath());
		var expected = """
			targetPath/
			├─ dir1/
			│  ├─ files_1_1
			│  ├─ files_1_2
			│  └─ files_1_3
			├─ dir2/
			│  ├─ files_2_1
			│  ├─ files_2_2
			│  └─ files_2_3
			└─ dir3/
			   ├─ files_3_1
			   ├─ files_3_2
			   └─ files_3_3""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void alphabetical_reversed() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withFileSort(PrettyPrintOptions.Sorts.ALPHABETICAL_ORDER.reversed()))
			.build();

		var result = printer.prettyPrint(buildDefaultPath());
		var expected = """
			targetPath/
			├─ dir3/
			│  ├─ files_3_3
			│  ├─ files_3_2
			│  └─ files_3_1
			├─ dir2/
			│  ├─ files_2_3
			│  ├─ files_2_2
			│  └─ files_2_1
			└─ dir1/
			   ├─ files_1_3
			   ├─ files_1_2
			   └─ files_1_1""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void fileSize() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withFileSort(PrettyPrintOptions.Sorts.FILE_SIZE))
			.build();

		var result = printer.prettyPrint(Path.of("src/example/resources/file_sort"));
		var expected = """
			file_sort/
			├─ dir_A/
			│  ├─ file_A_1
			│  ├─ file_A_3
			│  └─ file_A_2
			├─ dir_B/
			│  ├─ file_B_1
			│  ├─ file_B_3
			│  └─ file_B_2
			└─ dir_C/
			   ├─ file_C_1
			   ├─ file_C_3
			   └─ file_C_2""";
		assertThat(result).isEqualTo(expected);
	}

}
