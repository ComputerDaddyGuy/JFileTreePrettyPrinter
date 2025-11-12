package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathSorts;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import java.nio.file.Path;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FilteringTest {

	@TempDir
	private Path root;

	@Test
	void some_file_match() {
		var path = FileStructureCreator.forTargetPath(root)
			.createFile("file1.java")
			.createFile("file2.php")
			.createFile("file3.java")
			.createFile("file4.c")
			.createFile("file5.java")
			.getPath();

		var fileFilter = PathMatchers.hasExtension("java");

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filterFiles(fileFilter))
			.build();

		var result = printer.prettyPrint(path);

		var expected = """
			targetPath/
			├─ file1.java
			├─ file3.java
			└─ file5.java""";

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void no_file_match() {
		var path = FileStructureCreator.forTargetPath(root)
			.createFile("file1.java")
			.createFile("file2.php")
			.createFile("file3.java")
			.createFile("file4.c")
			.createFile("file5.java")
			.getPath();

		var fileFilter = PathMatchers.hasExtension("exe");

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filterFiles(fileFilter))
			.build();

		var result = printer.prettyPrint(path);

		var expected = """
			targetPath/""";

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void some_directory_match() {
		var path = FileStructureCreator.forTargetPath(root)
			.createDirectory("dir1_A")
			.createDirectory("dir2_B")
			.createDirectory("dir3_A")
			.createDirectory("dir4_C")
			.createDirectory("dir5_A")
			.getPath();

		var dirFilter = PathMatchers.hasNameEndingWith("_A");

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filterDirectories(dirFilter))
			.build();

		var result = printer.prettyPrint(path);

		var expected = """
			targetPath/
			├─ dir1_A/
			├─ dir3_A/
			└─ dir5_A/""";

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void no_directory_match() {
		var path = FileStructureCreator.forTargetPath(root)
			.createDirectory("dir1_A")
			.createDirectory("dir2_B")
			.createDirectory("dir3_A")
			.createDirectory("dir4_C")
			.createDirectory("dir5_A")
			.getPath();

		var dirFilter = PathMatchers.hasNameEndingWith("_Z");

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filterDirectories(dirFilter))
			.build();

		var result = printer.prettyPrint(path);

		var expected = """
			targetPath/""";

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void matchingFiles_inside_nonMathDir_are_not_displayed() {
		// @formatter:off
		var path = FileStructureCreator.forTargetPath(root)
				.createAndEnterDirectory("dir_A")
					.createFile("file_A1.java")
				.up()
				.createAndEnterDirectory("dir_B")
					.createFile("file_B1.java")
				.up()
			.getPath();
		// @formatter:on

		var dirFilter = PathMatchers.hasNameEndingWith("_A");
		var fileFilter = PathMatchers.hasExtension("java");

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filterDirectories(dirFilter))
			.customizeOptions(options -> options.filterFiles(fileFilter))
			.build();

		var result = printer.prettyPrint(path);

		var expected = """
			targetPath/
			└─ dir_A/
			   └─ file_A1.java""";

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void matchingDirectories_inside_nonMathDir_are_not_displayed() {
		// @formatter:off
		var path = FileStructureCreator.forTargetPath(root)
			.createAndEnterDirectory("dir_A")
				.createDirectory("dir_A_sub")
			.up()
			.createAndEnterDirectory("dir_B")
				.createDirectory("dir_B_sub")
			.up()
			.getPath();
		// @formatter:on

		var dirFilter = PathMatchers.anyOf(
			PathMatchers.hasNameEndingWith("_A"),
			PathMatchers.hasNameEndingWith("_sub")
		);

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filterDirectories(dirFilter))
			.build();

		var result = printer.prettyPrint(path);

		var expected = """
			targetPath/
			└─ dir_A/
			   └─ dir_A_sub/""";

		assertThat(result).isEqualTo(expected);
	}

	@Nested
	class WithSorting {

		@Test
		void combine_filtering_and_sorting() {
		// @formatter:off
		var path = FileStructureCreator.forTargetPath(root)
				.createAndEnterDirectory("dir_A")
					.createFile("file_A3.java")
					.createFile("file_A2.java")
					.createFile("file_Z4.java")
					.createFile("file_A1.java")
				.up()
				.createAndEnterDirectory("dir_B")
					.createFile("file_B1.java")
				.up()
			.getPath();
		// @formatter:on

			var fileFilter = PathMatchers.hasExtension("java");

			FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.sort(PathSorts.ALPHABETICAL.reversed()))
				.customizeOptions(options -> options.filterFiles(fileFilter))
				.build();

			var result = printer.prettyPrint(path);
			var expected = """
				targetPath/
				├─ dir_B/
				│  └─ file_B1.java
				└─ dir_A/
				   ├─ file_Z4.java
				   ├─ file_A3.java
				   ├─ file_A2.java
				   └─ file_A1.java""";
			assertThat(result).isEqualTo(expected);
		}

	}

	@Nested
	class WithChildLimit {

		private void combine_filtering_and_childLimit(int limit, String expected) {
			// @formatter:off
			var path = FileStructureCreator.forTargetPath(root)
					.createAndEnterDirectory("dir_A")
						.createFile("file_A1.java")
						.createFile("file_A2.java")
						.createFile("file_A3.java")
					.up()
					.createAndEnterDirectory("dir_B")
						.createFile("file_B1.java")
						.createFile("file_B2.java")
						.createFile("file_B3.java")
					.up()
				.getPath();
			// @formatter:on

			var fileFilter = PathMatchers.hasExtension("java");

			FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.withChildLimit(limit))
				.customizeOptions(options -> options.filterFiles(fileFilter))
				.build();

			var result = printer.prettyPrint(path);
			assertThat(result).isEqualTo(expected);
		}

		@Test
		void childLimit_1() {
			var expected = """
				targetPath/
				├─ dir_A/
				│  ├─ file_A1.java
				│  └─ ...
				└─ ...""";

			combine_filtering_and_childLimit(1, expected);
		}

		@Test
		void childLimit_2() {
			var expected = """
				targetPath/
				├─ dir_A/
				│  ├─ file_A1.java
				│  ├─ file_A2.java
				│  └─ ...
				└─ dir_B/
				   ├─ file_B1.java
				   ├─ file_B2.java
				   └─ ...""";

			combine_filtering_and_childLimit(2, expected);
		}

		@Test
		void childLimit_3() {
			var expected = """
				targetPath/
				├─ dir_A/
				│  ├─ file_A1.java
				│  ├─ file_A2.java
				│  └─ file_A3.java
				└─ dir_B/
				   ├─ file_B1.java
				   ├─ file_B2.java
				   └─ file_B3.java""";

			combine_filtering_and_childLimit(3, expected);
		}

	}

	@Nested
	class WithCompactDir {

		@Test
		void nominal() {

			// @formatter:off
			var path = FileStructureCreator
				.forTargetPath(root)
				.createAndEnterDirectory("level1-1")
					.createAndEnterDirectory("level2-1")
						.createAndEnterDirectory("level3-1")
							.createFile("level3-1#1.java")
							.createFile("level3-1#2.java")
							.createFile("level3-1#3.java")
							.createAndEnterDirectory("level4-1")
								.createAndEnterDirectory("level5-1")
									.createFile("level5-1#1.java")
									.createFile("level5-1#2.java")
									.createFile("level5-1#3.java")
								.up() // level5
							.up() // level4
							.createAndEnterDirectory("level4-2")
								.createAndEnterDirectory("level5-2")
									.createFile("level5-2#1.java")
									.createFile("level5-2#2.java")
									.createFile("level5-2#3.java")
								.up() // level5
							.up() // level4
						.up() // level3
					.up() // level2
				.up() // level1
				.getPath();
			// @formatter:on

			var dirFilter = PathMatchers.hasNameEndingWith("1");
			var fileFilter = PathMatchers.hasExtension("java");

			FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.withCompactDirectories(true))
				.customizeOptions(options -> options.filterDirectories(dirFilter))
				.customizeOptions(options -> options.filterFiles(fileFilter))
				.build();

			var result = printer.prettyPrint(path);
			var expected = """
				targetPath/
				└─ level1-1/level2-1/level3-1/
				   ├─ level3-1#1.java
				   ├─ level3-1#2.java
				   ├─ level3-1#3.java
				   └─ level4-1/level5-1/
				      ├─ level5-1#1.java
				      ├─ level5-1#2.java
				      └─ level5-1#3.java""";
			assertThat(result).isEqualTo(expected);
		}

	}

}
