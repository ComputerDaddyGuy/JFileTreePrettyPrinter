package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.ExternalOptionsReader;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
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

	private static final String DEFAULT_OPTION_FILENAME = ".prettyprint";

	@Nullable
	@Parameters(index = "0", description = "The path to pretty print", arity = "0..1")
	private File target;

	@Nullable
	@Option(names = { "-o", "--options" }, paramLabel = "OPTIONS", description = "the options file", arity = "0")
	private File optionsFile;

	@Option(names = { "-d", "--debug" }, description = "debug mode")
	private boolean debug;

	@Override
	public Integer call() throws Exception {

		var output = new ConsoleOutput(debug);

		var targetPath = detectTargetPath(output);
		if (!targetPath.toFile().exists()) {
			output.printError("Path not found: %s", targetPath);
			return 1;
		}

		var options = detectOptions(targetPath, output);

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

	private PrettyPrintOptions detectOptions(Path targetPath, ConsoleOutput output) {

		var reader = new ExternalOptionsReader(output);
		if (optionsFile != null) {
			var path = optionsFile.toPath().toAbsolutePath().normalize();
			var options = reader.readOptions(targetPath, path);
			if (options == null) {
				System.exit(1);
			}
			return options;
		} else {
			var potentialOptions = new ArrayList<Path>();
			potentialOptions.add(targetPath.resolve(DEFAULT_OPTION_FILENAME));
			potentialOptions.add(Path.of(".").resolve(DEFAULT_OPTION_FILENAME));
			potentialOptions.add(Path.of(System.getProperty("user.home")).resolve(DEFAULT_OPTION_FILENAME));
			var options = reader.readOptions(targetPath, potentialOptions);
			if (options != null) {
				return options;
			}
			output.printDebug("No options file provided/found!");
			return PrettyPrintOptions.createDefault();
		}

	}

}
