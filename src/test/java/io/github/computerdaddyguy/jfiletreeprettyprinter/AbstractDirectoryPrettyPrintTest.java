package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.RenderingOptions.TreeFormat;
import java.nio.file.Path;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

abstract class AbstractDirectoryPrettyPrintTest {

	@TempDir
	protected static Path root;

	protected static Path targetPath;

	protected FileTreePrettyPrinter customize(Function<PrettyPrintOptions, PrettyPrintOptions> customizer) {
		var builder = FileTreePrettyPrinter.builder();
		builder.customizeOptions(customizer);
		return builder.build();
	}

	protected void run(Function<PrettyPrintOptions, PrettyPrintOptions> customizer, String expected) {
		var printer = customize(customizer);
		var actual = printer.prettyPrint(targetPath);
		assertThat(actual).isEqualTo(expected);
	}

	// ---------------------------------------------------------------------------------
	// DEFAULT

	@Test
	void defaultOptions() {
		run(
			options -> options,
			defaultOptionsExpected()
		);
	}

	abstract String defaultOptionsExpected();

	// ---------------------------------------------------------------------------------
	// EMOJI

	@Test
	void withEmoji() {
		run(
			options -> options.withEmojis(true),
			withEmojiExpected()
		);
	}

	abstract String withEmojiExpected();

	// ---------------------------------------------------------------------------------
	// DEPTH FORMAT CLASSIC_ASCII

	@Test
	void withTreeFormatClassicAscii() {
		run(
			options -> options.withTreeFormat(TreeFormat.CLASSIC_ASCII),
			withTreeFormatClassicAsciiExpected()
		);
	}

	abstract String withTreeFormatClassicAsciiExpected();

	// ---------------------------------------------------------------------------------
	// LIMIT 0

	@Test
	void withLimit0() {
		run(
			options -> options.withChildrenLimit(0),
			withLimit0Expected()
		);
	}

	abstract String withLimit0Expected();

	// ---------------------------------------------------------------------------------
	// LIMIT 1

	@Test
	void withLimit1() {
		run(
			options -> options.withChildrenLimit(1),
			withLimit1Expected()
		);
	}

	abstract String withLimit1Expected();

	// ---------------------------------------------------------------------------------
	// LIMIT 2

	@Test
	void withLimit2() {
		run(
			options -> options.withChildrenLimit(2),
			withLimit2Expected()
		);
	}

	abstract String withLimit2Expected();

	// ---------------------------------------------------------------------------------
	// LIMIT 3

	@Test
	void withLimit3() {
		run(
			options -> options.withChildrenLimit(3),
			withLimit3Expected()
		);
	}

	abstract String withLimit3Expected();

	// ---------------------------------------------------------------------------------
	// COMPACT DIRECTORIES

	@Test
	void withCompactDirectories() {
		run(
			options -> options.withCompactDirectories(true),
			withCompactDirectoriesExpected()
		);
	}

	abstract String withCompactDirectoriesExpected();

}
