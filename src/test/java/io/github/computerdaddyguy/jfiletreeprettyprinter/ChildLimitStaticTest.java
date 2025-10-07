package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChildLimitStaticTest {

	@TempDir
	private Path root;

	private FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
		.customizeOptions(options -> options.withChildLimit(3))
		.build();

	@Test
	void nominal() {
		var path = FileStructures.emptyDirectory(root);
		var result = printer.prettyPrint(path);
		var expected = "targetPath/";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWith_1_file() {
		var path = FileStructures.simpleDirectoryWithFiles(root, 1);
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			└─ file1""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWith_limit_minus_one_files() {
		var path = FileStructures.simpleDirectoryWithFiles(root, 2);
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ file1
			└─ file2""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWith_exact_limit_of_files() {
		var path = FileStructures.simpleDirectoryWithFiles(root, 3);
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ file1
			├─ file2
			└─ file3""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWith_1_extra_file() {
		var path = FileStructures.simpleDirectoryWithFiles(root, 4);
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ file1
			├─ file2
			├─ file3
			└─ ...""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWith_2_extra_files() {
		var path = FileStructures.simpleDirectoryWithFiles(root, 5);
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ file1
			├─ file2
			├─ file3
			└─ ...""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWith_2_extra_files_and_1_extra_folder() {
		var path = FileStructures.simpleDirectoryWithFilesAndFolders(root, 5, 1);
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ file1
			├─ file2
			├─ file3
			└─ ...""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void dirWith_2_extra_files_and_2_extra_folders() {
		var path = FileStructures.simpleDirectoryWithFilesAndFolders(root, 5, 2);
		var result = printer.prettyPrint(path);
		var expected = """
			targetPath/
			├─ file1
			├─ file2
			├─ file3
			└─ ...""";
		assertThat(result).isEqualTo(expected);
	}

}
