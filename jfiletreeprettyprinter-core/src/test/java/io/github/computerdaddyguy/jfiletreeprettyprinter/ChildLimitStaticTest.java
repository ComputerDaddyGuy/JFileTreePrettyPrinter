package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChildLimitStaticTest {

	@TempDir
	private Path root;

	@Test
	void empty_directory() {
		var path = FileStructures.emptyDirectory(root);
		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(3))
			.build();
		var result = printer.prettyPrint(path);
		var expected = "targetPath/";
		assertThat(result).isEqualTo(expected);
	}

	private void doTest(int limit, String expected) {
		var path = FileStructures.simpleDirectoryWithFiles(root, 3);

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(limit))
			.build();

		var result = printer.prettyPrint(path);

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void test_0_file() {
		var expected = """
			targetPath/
			└─ ...""";
		doTest(0, expected);
	}

	@Test
	void test_1_file() {
		var expected = """
			targetPath/
			├─ file1
			└─ ...""";
		doTest(1, expected);
	}

	@Test
	void test_limit_minus_one_files() {
		var expected = """
			targetPath/
			├─ file1
			├─ file2
			└─ ...""";
		doTest(2, expected);
	}

	@Test
	void test_exact_limit_of_files() {
		var expected = """
			targetPath/
			├─ file1
			├─ file2
			└─ file3""";
		doTest(3, expected);
	}

}
