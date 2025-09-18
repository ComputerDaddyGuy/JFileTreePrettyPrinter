package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SortingTest {

	@TempDir
	private Path root;

	@Test
	void example() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(PrettyPrintOptions.Sorts.DIRECTORY_FIRST))
			.build();

		var result = printer.prettyPrint(Path.of("src/example/resources/sorting"));
		var expected = """
			sorting/
			├─ c_dir/
			│  └─ c_file
			├─ d_dir/
			│  ├─ d_b_dir/
			│  │  └─ d_b_file
			│  └─ d_a_file
			├─ a_file
			├─ b_file
			├─ x_file
			└─ y_file""";
		assertThat(result).isEqualTo(expected);
	}

	// ---------- Alphabetical ----------

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
			.customizeOptions(options -> options.sort(PrettyPrintOptions.Sorts.BY_NAME.reversed()))
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

	// ---------- File size ----------

	@Test
	void fileSize() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(PrettyPrintOptions.Sorts.BY_FILE_SIZE))
			.build();

		var result = printer.prettyPrint(Path.of("src/example/resources/sorting"));
		var expected = """
			sorting/
			├─ c_dir/
			│  └─ c_file
			├─ d_dir/
			│  ├─ d_b_dir/
			│  │  └─ d_b_file
			│  └─ d_a_file
			├─ y_file
			├─ x_file
			├─ a_file
			└─ b_file""";
		assertThat(result).isEqualTo(expected);
	}

	// ---------- Directories first & last ----------

	private Path build_directoryFirstOrLast_paths() {
		// @formatter:off
		return FileStructureCreator.forTargetPath(root)
			.createFile("c_file")
			.createAndEnterDirectory("d_dir")
				.createFile("d1_file")
				.createDirectory("d2_directory")
				.up()
			.createFile("a_file")
			.createAndEnterDirectory("b_dir")
				.createFile("b1_file")
				.createDirectory("b2_directory")
				.createFile("b3_file")
				.createDirectory("b4_directory")
				.up()
			.getPath();
		// @formatter:on
	}

	@Test
	void directoriesFirst() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(PrettyPrintOptions.Sorts.DIRECTORY_FIRST))
			.build();

		var result = printer.prettyPrint(build_directoryFirstOrLast_paths());
		var expected = """
			targetPath/
			├─ b_dir/
			│  ├─ b2_directory/
			│  ├─ b4_directory/
			│  ├─ b1_file
			│  └─ b3_file
			├─ d_dir/
			│  ├─ d2_directory/
			│  └─ d1_file
			├─ a_file
			└─ c_file""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void directoriesLast() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(PrettyPrintOptions.Sorts.DIRECTORY_LAST))
			.build();

		var result = printer.prettyPrint(build_directoryFirstOrLast_paths());
		var expected = """
			targetPath/
			├─ a_file
			├─ c_file
			├─ b_dir/
			│  ├─ b1_file
			│  ├─ b3_file
			│  ├─ b2_directory/
			│  └─ b4_directory/
			└─ d_dir/
			   ├─ d1_file
			   └─ d2_directory/""";
		assertThat(result).isEqualTo(expected);
	}

	// ---------- Extensions ----------

	private Path build_extension_paths() {
		// @formatter:off
		return FileStructureCreator.forTargetPath(root)
			.createFile("c_file.cpp")
			.createFile("a_file.c")
			.createFile("b_file.c")
			.createFile("no_extension_1")
			.createFile("f_file.tar.gz")
			.createFile("e_file.tar.gz")
			.createFile("g_file.tar.gz2")
			.createFile("no_extension_2")
			.createFile("d_file.gz")
			.createDirectory("dir_1.c")
			.createDirectory("dir_2")
			.getPath();
		// @formatter:on
	}

	@Test
	void byExtension() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(PrettyPrintOptions.Sorts.BY_EXTENSION))
			.build();

		var result = printer.prettyPrint(build_extension_paths());
		var expected = """
			targetPath/
			├─ dir_1.c/
			├─ dir_2/
			├─ no_extension_1
			├─ no_extension_2
			├─ a_file.c
			├─ b_file.c
			├─ c_file.cpp
			├─ d_file.gz
			├─ e_file.tar.gz
			├─ f_file.tar.gz
			└─ g_file.tar.gz2""";
		assertThat(result).isEqualTo(expected);
	}

}
