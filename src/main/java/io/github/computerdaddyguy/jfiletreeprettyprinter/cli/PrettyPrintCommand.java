package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.ExternalOptionsException;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.ExternalOptionsMapper;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.ExternalOptionsReader;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import jakarta.validation.ConstraintViolationException;
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

	private final ExternalOptionsReader reader;
	private final ExternalOptionsMapper mapper;

	public PrettyPrintCommand(ExternalOptionsReader reader, ExternalOptionsMapper mapper) {
		this.reader = Objects.requireNonNull(reader, "reader is null");
		this.mapper = Objects.requireNonNull(mapper, "mapper is null");
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

		var output = new ConsoleOutput(debug);

		var targetPath = detectTargetPath(output);
		if (!targetPath.toFile().exists()) {
			output.printError("Path not found: %s", targetPath);
			return 1;
		}

		PrettyPrintOptions options;
		try {
			Path optionsPath = optionsFile == null ? null : optionsFile.toPath().toAbsolutePath().normalize();
			var externalOptions = reader.readExternalOptions(output, targetPath, optionsPath);
			options = mapper.mapToOptions(targetPath, externalOptions);
		} catch (ExternalOptionsException e) {
			output.printError(e.getMessage() + ": " + optionsFile.toString());
			return 1;
		} catch (ConstraintViolationException e) {
			output.printError(e.getMessage() + ": " + optionsFile.toString());
			return 1;
		}

		try {
			var printer = FileTreePrettyPrinter.builder().withOptions(options).build();
			var result = printer.prettyPrint(targetPath);
			output.print(result);
			return 0;
		} catch (Exception e) {
			output.printError("Error while pretty printing: " + e.getMessage());
			return 1;
		}
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
