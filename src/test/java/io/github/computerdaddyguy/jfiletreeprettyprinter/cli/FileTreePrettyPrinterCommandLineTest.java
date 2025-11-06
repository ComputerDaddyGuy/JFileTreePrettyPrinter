package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.exception.DefaultExecutionExceptionHandler;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io.ConsoleOutput;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io.DefaultConsoleOutput;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.OptionsLoader;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.ChildLimits;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FileTreePrettyPrinterCommandLineTest {

	private ByteArrayOutputStream out_and_err;

	private ConsoleOutput output;

	@BeforeEach
	void prepareSystemOut() {
		out_and_err = new ByteArrayOutputStream();

		output = new DefaultConsoleOutput(new PrintStream(out_and_err), new PrintStream(out_and_err));
	}

	private String[] buildCliArgs(String targetPath, @Nullable String optionsPath) {
		if (optionsPath == null) {
			return new String[] { targetPath };
		}
		return new String[] { targetPath, "-o", optionsPath };
//		return new String[] { targetPath, "-o", optionsPath, "-d" };
	}

	private void runCliTest(FileTreePrettyPrinterCommandLine cli, FileTreePrettyPrinter ref, String[] args, String targetPath) {

		cli.executeCommand(args);

		var allWrittenLines = new String(out_and_err.toByteArray());
		assertThat(allWrittenLines).isEqualTo(ref.prettyPrint(targetPath) + "\n");
	}

	@Test
	void no_options() throws IOException {
		String targetPath = "src/test/resources/cli/base";
		String optionsPath = null;
		String[] args = buildCliArgs(targetPath, optionsPath);

		var optionsLoader = OptionsLoader.createDefault(output);
		var exHandler = new DefaultExecutionExceptionHandler(output);
		var cli = new FileTreePrettyPrinterCommandLine(output, optionsLoader, exHandler);

		var ref = FileTreePrettyPrinter.createDefault();

		runCliTest(cli, ref, args, targetPath);
	}

	@Nested
	class Emojis {

		@Test
		void emojis() throws IOException {
			String targetPath = "src/test/resources/cli/base";
			String optionsPath = "src/test/resources/cli/options/emojis.yaml";
			String[] args = buildCliArgs(targetPath, optionsPath);

			var optionsLoader = OptionsLoader.createDefault(output);
			var exHandler = new DefaultExecutionExceptionHandler(output);
			var cli = new FileTreePrettyPrinterCommandLine(output, optionsLoader, exHandler);

			var ref = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.withDefaultEmojis())
				.build();

			runCliTest(cli, ref, args, targetPath);
		}

	}

	@Nested
	class ChildLimit {

		@Test
		void static_limit() throws IOException {
			String targetPath = "src/test/resources/cli/base";
			String optionsPath = "src/test/resources/cli/options/childLimit_static.yaml";
			String[] args = buildCliArgs(targetPath, optionsPath);

			var optionsLoader = OptionsLoader.createDefault(output);
			var exHandler = new DefaultExecutionExceptionHandler(output);
			var cli = new FileTreePrettyPrinterCommandLine(output, optionsLoader, exHandler);

			var ref = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.withChildLimit(2))
				.build();

			runCliTest(cli, ref, args, targetPath);
		}

		@Test
		void dynamic_limit_glob() throws IOException {
			String targetPath = "src/test/resources/cli/base";
			String optionsPath = "src/test/resources/cli/options/childLimit_dynamic_glob.yaml";
			String[] args = buildCliArgs(targetPath, optionsPath);

			var optionsLoader = OptionsLoader.createDefault(output);
			var exHandler = new DefaultExecutionExceptionHandler(output);
			var cli = new FileTreePrettyPrinterCommandLine(output, optionsLoader, exHandler);

			var childLimit = ChildLimits.builder()
				.add(PathMatchers.hasNameMatchingGlob("*_a"), 2)
				.add(PathMatchers.hasRelativePathMatchingGlob(Path.of(targetPath), "/*_b"), 3)
				.build();
			var ref = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.withChildLimit(childLimit))
				.build();

			runCliTest(cli, ref, args, targetPath);
		}

		@Test
		void dynamic_limit_everything() throws IOException {
			String targetPath = "src/test/resources/cli/base";
			String optionsPath = "src/test/resources/cli/options/childLimit_dynamic_everything.yaml";
			String[] args = buildCliArgs(targetPath, optionsPath);

			var optionsLoader = OptionsLoader.createDefault(output);
			var exHandler = new DefaultExecutionExceptionHandler(output);
			var cli = new FileTreePrettyPrinterCommandLine(output, optionsLoader, exHandler);

			var childLimit = ChildLimits.builder()
				.add(p -> 2)
				.build();
			var ref = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.withChildLimit(childLimit))
				.build();

			runCliTest(cli, ref, args, targetPath);
		}

		@Test
		void dynamic_limit_allOf() throws IOException {
			String targetPath = "src/test/resources/cli/base";
			String optionsPath = "src/test/resources/cli/options/childLimit_dynamic_allOf.yaml";
			String[] args = buildCliArgs(targetPath, optionsPath);

			var optionsLoader = OptionsLoader.createDefault(output);
			var exHandler = new DefaultExecutionExceptionHandler(output);
			var cli = new FileTreePrettyPrinterCommandLine(output, optionsLoader, exHandler);

			var childLimit = ChildLimits.builder()
				.add(PathMatchers.allOf(PathMatchers.hasNameMatchingGlob("*3"), PathMatchers.hasNameMatchingGlob("*xxx*")), 2)
				.add(PathMatchers.allOf(PathMatchers.hasNameMatchingGlob("*3"), PathMatchers.hasNameMatchingGlob("*z*")), 3)
				.build();
			var ref = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.withChildLimit(childLimit))
				.build();

			runCliTest(cli, ref, args, targetPath);
		}

		@Test
		void dynamic_limit_anyOf() throws IOException {
			String targetPath = "src/test/resources/cli/base";
			String optionsPath = "src/test/resources/cli/options/childLimit_dynamic_anyOf.yaml";
			String[] args = buildCliArgs(targetPath, optionsPath);

			var optionsLoader = OptionsLoader.createDefault(output);
			var exHandler = new DefaultExecutionExceptionHandler(output);
			var cli = new FileTreePrettyPrinterCommandLine(output, optionsLoader, exHandler);

			var childLimit = ChildLimits.builder()
				.add(PathMatchers.anyOf(PathMatchers.hasNameMatchingGlob("*3"), PathMatchers.hasNameMatchingGlob("*xxx*")), 2)
				.add(PathMatchers.anyOf(PathMatchers.hasNameMatchingGlob("*3"), PathMatchers.hasNameMatchingGlob("*z*")), 3)
				.build();
			var ref = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.withChildLimit(childLimit))
				.build();

			runCliTest(cli, ref, args, targetPath);
		}

		@Test
		void dynamic_limit_noneOf() throws IOException {
			String targetPath = "src/test/resources/cli/base";
			String optionsPath = "src/test/resources/cli/options/childLimit_dynamic_noneOf.yaml";
			String[] args = buildCliArgs(targetPath, optionsPath);

			var optionsLoader = OptionsLoader.createDefault(output);
			var exHandler = new DefaultExecutionExceptionHandler(output);
			var cli = new FileTreePrettyPrinterCommandLine(output, optionsLoader, exHandler);

			var childLimit = ChildLimits.builder()
				.add(PathMatchers.noneOf(PathMatchers.hasNameMatchingGlob("*a"), PathMatchers.hasNameMatchingGlob("folder*")), 2)
				.add(PathMatchers.noneOf(PathMatchers.hasNameMatchingGlob("*a"), PathMatchers.hasNameMatchingGlob("*xxx*")), 3)
				.build();
			var ref = FileTreePrettyPrinter.builder()
				.customizeOptions(options -> options.withChildLimit(childLimit))
				.build();

			runCliTest(cli, ref, args, targetPath);
		}

	}

}
