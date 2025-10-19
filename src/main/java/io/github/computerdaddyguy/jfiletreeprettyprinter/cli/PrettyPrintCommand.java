package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.ExternalOptionsReader;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

// @formatter:off
@Command(
	name = "prettyprint", 
	mixinStandardHelpOptions = true, 
	version = "checksum 4.0", 
	description = "Prints the checksum (SHA-256 by default) of a file to STDOUT."
)
// @formatter:on
public class PrettyPrintCommand implements Callable<Integer> {

	@Parameters(index = "0", description = "The path to pretty print", arity = "0")
	private File file;

	@Option(names = { "-o", "--options" }, paramLabel = "OPTIONS", description = "the options file", arity = "0")
	private File optionsFile;

	@Override
	public Integer call() throws Exception {

		LogManager.getLogManager().reset();
		Logger.getLogger("org.hibernate.validator").setLevel(Level.OFF);

		var reader = new ExternalOptionsReader();
		reader.readOptions();

		Path path = detectPathToPrint();
		if (optionsFile != null) {
			path = optionsFile.toPath();
		}
		try {
			var printer = FileTreePrettyPrinter.createDefault();
			var result = printer.prettyPrint(path);
			System.out.println(result);
		} catch (Exception e) {
			System.err.print("Error while pretty printing: " + e.getMessage());
			return 1;
		}

		return 0;
	}

	private static Path detectPathToPrint() {
		for (var p : List.of("tmp", "target/tmp")) {
			var path = Path.of(p);
			if (path.toFile().exists()) {
				return path;
			}
		}
		return Path.of(".");

	}

}
