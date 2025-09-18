package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions.Sorts;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FilteringTest {

	@TempDir
	private Path root;

	@Test
	void example() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.createDefault();
		var filter = PathPredicates.hasExtension("java");

		var result = printer.prettyPrint("src/example/resources/filtering", filter);
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ file_E.java
			├─ dir_with_nested_java_files/
			│  └─ nested_dir_with_java_files/
			│     ├─ file_G.java
			│     └─ file_J.java
			└─ file_A.java""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_dir_match() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.createDefault();
		var filter = PathPredicates.hasNameEndingWith("no_java_file");

		var result = printer.prettyPrint("src/example/resources/filtering", filter);
		var expected = """
			filtering/
			├─ dir_with_nested_java_files/
			│  └─ nested_dir_with_no_java_file/
			└─ dir_with_no_java_file/""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_and_sorting() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(Sorts.BY_NAME.reversed()))
			.build();
		var filter = PathPredicates.hasExtension("java");

		var result = printer.prettyPrint("src/example/resources/filtering", filter);
		var expected = """
			filtering/
			├─ file_A.java
			├─ dir_with_nested_java_files/
			│  └─ nested_dir_with_java_files/
			│     ├─ file_J.java
			│     └─ file_G.java
			└─ dir_with_java_files/
			   ├─ file_E.java
			   └─ file_B.java""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_childrenLimit_1() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildrenLimit(1))
			.build();
		var filter = PathPredicates.hasExtension("java");

		var result = printer.prettyPrint("src/example/resources/filtering", filter);
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ ... (1 file skipped)
			└─ ... (1 file and 1 directory skipped)""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_childrenLimit_2() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildrenLimit(2))
			.build();
		var filter = PathPredicates.hasExtension("java");

		var result = printer.prettyPrint("src/example/resources/filtering", filter);
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ file_E.java
			├─ dir_with_nested_java_files/
			│  └─ nested_dir_with_java_files/
			│     ├─ file_G.java
			│     └─ file_J.java
			└─ ... (1 file skipped)""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_childrenLimit_3() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildrenLimit(3))
			.build();
		var filter = PathPredicates.hasExtension("java");

		var result = printer.prettyPrint("src/example/resources/filtering", filter);
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ file_E.java
			├─ dir_with_nested_java_files/
			│  └─ nested_dir_with_java_files/
			│     ├─ file_G.java
			│     └─ file_J.java
			└─ file_A.java""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_compact_dir() {

		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withCompactDirectories(true))
			.build();
		var filter = PathPredicates.hasExtension("java");

		var result = printer.prettyPrint("src/example/resources/filtering", filter);
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ file_E.java
			├─ dir_with_nested_java_files/nested_dir_with_java_files/
			│  ├─ file_G.java
			│  └─ file_J.java
			└─ file_A.java""";
		assertThat(result).isEqualTo(expected);
	}

}
