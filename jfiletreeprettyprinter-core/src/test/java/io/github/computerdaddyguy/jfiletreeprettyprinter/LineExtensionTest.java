package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.LineExtensions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;
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
	void compact_dir_first_dir() {

		Function<Path, String> lineExtension = LineExtensions.builder()
			.add(PathMatchers.hasName("dirA"), " // 1")
			.build();

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
	void compact_dir_empty_string_creates_line_break() {

		Function<Path, String> lineExtension = LineExtensions.builder()
			.addLineBreak(PathMatchers.hasName("dirA"))
			.build();

		var expected = """
			targetPath/
			├─ dirA/
			│  └─ dirB/dirC/
			│     ├─ file1
			│     ├─ file2
			│     └─ file3
			└─ dirX/""";

		compact_dir(lineExtension, expected);
	}

	@Test
	void compact_dir_middle_dir() {

		Function<Path, String> lineExtension = LineExtensions.builder()
			.add(PathMatchers.hasName("dirB"), " // 2")
			.build();

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

		Function<Path, String> lineExtension = LineExtensions.builder()
			.add(PathMatchers.hasName("dirC"), " // 3")
			.build();

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
