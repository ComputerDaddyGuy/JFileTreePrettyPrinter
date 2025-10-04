package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FilteringTest {

	@TempDir
	private Path root;

	@Test
	void example_file() {

		var filter = PathMatchers.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filterFiles(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ file_E.java
			├─ dir_with_nested_java_files/
			│  ├─ nested_dir_with_java_files/
			│  │  ├─ file_G.java
			│  │  └─ file_J.java
			│  └─ nested_dir_with_no_java_file/
			├─ dir_with_no_java_file/
			└─ file_A.java""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_dir() {

		var filter = PathMatchers.hasNameEndingWith("no_java_file");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filterDirectories(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
		var expected = """
			filtering/
			├─ dir_with_no_java_file/
			│  ├─ file_M.cpp
			│  └─ file_N.ts
			└─ file_A.java""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_and_sorting() {

		var filter = PathMatchers.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(PathSorts.ALPHABETICAL.reversed()))
			.customizeOptions(options -> options.filterFiles(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
		var expected = """
			filtering/
			├─ file_A.java
			├─ dir_with_no_java_file/
			├─ dir_with_nested_java_files/
			│  ├─ nested_dir_with_no_java_file/
			│  └─ nested_dir_with_java_files/
			│     ├─ file_J.java
			│     └─ file_G.java
			└─ dir_with_java_files/
			   ├─ file_E.java
			   └─ file_B.java""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_childLimit_1() {

		var filter = PathMatchers.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(1))
			.customizeOptions(options -> options.filterFiles(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ ... (1 file skipped)
			└─ ... (1 file and 2 directories skipped)""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_childLimit_2() {

		var filter = PathMatchers.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(2))
			.customizeOptions(options -> options.filterFiles(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ file_E.java
			├─ dir_with_nested_java_files/
			│  ├─ nested_dir_with_java_files/
			│  │  ├─ file_G.java
			│  │  └─ file_J.java
			│  └─ nested_dir_with_no_java_file/
			└─ ... (1 file and 1 directory skipped)""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_childLimit_3() {

		var filter = PathMatchers.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(3))
			.customizeOptions(options -> options.filterFiles(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ file_E.java
			├─ dir_with_nested_java_files/
			│  ├─ nested_dir_with_java_files/
			│  │  ├─ file_G.java
			│  │  └─ file_J.java
			│  └─ nested_dir_with_no_java_file/
			├─ dir_with_no_java_file/
			└─ ... (1 file skipped)""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_compact_dir() {

		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
			.createAndEnterDirectory("level1")
				.createAndEnterDirectory("level2-1")
					.createAndEnterDirectory("level3-1")
						.createAndEnterDirectory("level4-1")
							.createFiles("file4-1#", 3)
						.up() // level4
						.createAndEnterDirectory("level4-2")
						.createFiles("file4-2#", 3)
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
			└─ level1/level2-1/level3-1/level4-1/""";
		assertThat(result).isEqualTo(expected);
	}

}
