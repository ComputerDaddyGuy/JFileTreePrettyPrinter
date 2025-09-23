package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions.Sorts;
import org.junit.jupiter.api.Test;

class FilteringTest {

	@Test
	void example() {

		var filter = PathPredicates.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filter(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
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

		var filter = PathPredicates.hasNameEndingWith("no_java_file");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filter(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
		var expected = """
			filtering/
			├─ dir_with_nested_java_files/
			│  └─ nested_dir_with_no_java_file/
			└─ dir_with_no_java_file/""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_and_sorting() {

		var filter = PathPredicates.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(Sorts.BY_NAME.reversed()))
			.customizeOptions(options -> options.filter(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
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
	void example_childLimit_1() {

		var filter = PathPredicates.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(1))
			.customizeOptions(options -> options.filter(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ ... (1 file skipped)
			└─ ... (1 file and 1 directory skipped)""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_childLimit_2() {

		var filter = PathPredicates.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(2))
			.customizeOptions(options -> options.filter(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
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
	void example_childLimit_3() {

		var filter = PathPredicates.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(3))
			.customizeOptions(options -> options.filter(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
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

		var filter = PathPredicates.hasExtension("java");
		FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withCompactDirectories(true))
			.customizeOptions(options -> options.filter(filter))
			.build();

		var result = printer.prettyPrint("src/example/resources/filtering");
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
