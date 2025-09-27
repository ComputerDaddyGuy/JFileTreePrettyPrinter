package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LineExtensionTest {

	@TempDir
	private Path root;

	@Test
	void emptyDir() {
		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withLineExtension(p -> " /* YES ! */"))
			.build();

		var path = FileStructures.emptyDirectory(root);
		var result = printer.prettyPrint(path);
		var expected = "targetPath/ /* YES ! */";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void example_dir_match() {

		Function<Path, String> lineExtension = path -> {
			if (PathPredicates.isDirectory(path) && PathPredicates.hasName(path, "api")) {
				return "\t\t\t// All API code: controllers, etc.";
			}
			if (PathPredicates.isDirectory(path) && PathPredicates.hasName(path, "domain")) {
				return "\t\t\t// All domain code: value objects, etc.";
			}
			if (PathPredicates.isDirectory(path) && PathPredicates.hasName(path, "infra")) {
				return "\t\t\t// All infra code: database, email service, etc.";
			}
			if (PathPredicates.isFile(path) && PathPredicates.hasName(path, "application.properties")) {
				return "\t// Config file";
			}
			return null;
		};

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withLineExtension(lineExtension))
			.build();

		var result = printer.prettyPrint("src/example/resources/line_extension");
		var expected = """
			line_extension/
			└─ src/
			   └─ main/
			      ├─ java/
			      │  ├─ api/			// All API code: controllers, etc.
			      │  │  └─ Controller.java
			      │  ├─ domain/			// All domain code: value objects, etc.
			      │  │  └─ ValueObject.java
			      │  └─ infra/			// All infra code: database, email service, etc.
			      │     └─ Repository.java
			      └─ resources/
			         └─ application.properties	// Config file""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void compact_dir_first_dir() {

		Function<Path, String> lineExtension = p -> {
			if (PathPredicates.hasName(p, "dirA")) {
				return " // 1";
			}
			return null;
		};

		var expected = """
			targetPath/
			├─ dirA/ // 1
			│  └─ dirB/dirC/
			│     ├─ file1
			│     ├─ file2
			│     └─ file3
			└─ dirX/""";

		compact_dir(lineExtension, expected);
	}

	@Test
	void compact_dir_middle_dir() {

		Function<Path, String> lineExtension = p -> {
			if (PathPredicates.hasName(p, "dirB")) {
				return " // 2";
			}
			return null;
		};

		var expected = """
			targetPath/
			├─ dirA/dirB/ // 2
			│  └─ dirC/
			│     ├─ file1
			│     ├─ file2
			│     └─ file3
			└─ dirX/""";

		compact_dir(lineExtension, expected);
	}

	@Test
	void compact_dir_last_dir() {

		Function<Path, String> lineExtension = p -> {
			if (PathPredicates.hasName(p, "dirC")) {
				return " // 3";
			}
			return null;
		};

		var expected = """
			targetPath/
			├─ dirA/dirB/dirC/ // 3
			│  ├─ file1
			│  ├─ file2
			│  └─ file3
			└─ dirX/""";

		compact_dir(lineExtension, expected);
	}

	private void compact_dir(Function<Path, String> lineExtension, String expected) {

		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
			.createAndEnterDirectory("dirA")
				.createAndEnterDirectory("dirB")
					.createAndEnterDirectory("dirC")
						.createFiles("file", 3)
					.up()
				.up()
			.up()
			.createDirectory("dirX")
			.getPath();
		// @formatter:on

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withLineExtension(lineExtension))
			.customizeOptions(options -> options.withCompactDirectories(true))
			.build();

		var result = printer.prettyPrint(path);
		assertThat(result).isEqualTo(expected);
	}

}
