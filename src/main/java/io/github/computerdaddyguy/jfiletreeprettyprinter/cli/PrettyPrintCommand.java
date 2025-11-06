package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io.ConsoleOutput;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.OptionsLoader;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

// @formatter:off
@Command(
	name = "prettyprint", 
	mixinStandardHelpOptions = true, 
	versionProvider = ManifestVersionProvider.class,
	description = "Pretty-prints directory structure"
)
// @formatter:on
@NullMarked
class PrettyPrintCommand implements Callable<Integer> {

	private final ConsoleOutput output;
	private final OptionsLoader optionsLoader;

	public PrettyPrintCommand(ConsoleOutput output, OptionsLoader optionsLoader) {
		this.output = Objects.requireNonNull(output, "output is null");
		this.optionsLoader = Objects.requireNonNull(optionsLoader, "optionsLoader is null");
	}

	// ---------- CLI args ----------

	@Nullable
	@Parameters(index = "0", description = "The path to pretty print", arity = "0..1")
	private File target;

	@Nullable
	@Option(names = { "-o", "--options" }, paramLabel = "OPTIONS", description = "the options file", arity = "0")
	private File optionsFile;

	@Option(names = { "-d", "--debug" }, description = "debug mode")
	private boolean debug;

	// ---------- Command ----------

	@Override
	public Integer call() throws Exception {

		output.enableDebug(this.debug);

		var targetPath = detectTargetPath(output);
		if (!targetPath.toFile().exists()) {
			output.printError("Path not found: %s", targetPath);
			return 1;
		}

		Path optionsPath = optionsFile == null ? null : optionsFile.toPath().toAbsolutePath().normalize();
		PrettyPrintOptions options = optionsLoader.loadOptions(targetPath, optionsPath);

		var printer = FileTreePrettyPrinter.builder().withOptions(options).build();
		var result = printer.prettyPrint(targetPath);
		output.print(result);

		return 0;
	}

	private Path detectTargetPath(ConsoleOutput output) {
		Path path = null;
		if (target != null) {
			path = target.toPath();
		} else {
			output.printDebug("No target provided: use current directory");
			path = Path.of(".");
		}
		path = path.toAbsolutePath().normalize();
		output.printDebug("Target path: %s", path);
		return path;
	}

}
