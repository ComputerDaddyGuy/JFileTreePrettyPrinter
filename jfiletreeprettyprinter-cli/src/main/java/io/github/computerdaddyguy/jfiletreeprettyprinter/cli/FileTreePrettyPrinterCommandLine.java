package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.exception.DefaultExecutionExceptionHandler;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io.ConsoleOutput;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.OptionsLoader;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;

@NullMarked
class FileTreePrettyPrinterCommandLine {

	private final CommandLine cmd;

	FileTreePrettyPrinterCommandLine(ConsoleOutput output, OptionsLoader optionsLoader, IExecutionExceptionHandler exHandler) {
		super();
		Objects.requireNonNull(output, "output is null");
		Objects.requireNonNull(optionsLoader, "optionsLoader is null");
		Objects.requireNonNull(exHandler, "exHandler is null");

		cmd = new CommandLine(new PrettyPrintCommand(output, optionsLoader));
		cmd.setExecutionExceptionHandler(exHandler);
	}

	int executeCommand(String[] args) {
		return cmd.execute(args);
	}

	// -------------------------------------------------------------------------------

	public static void main(String[] args) {

		var output = ConsoleOutput.createDefault();
		var optionsLoader = OptionsLoader.createDefault(output);
		var exHandler = new DefaultExecutionExceptionHandler(output);

		var cli = new FileTreePrettyPrinterCommandLine(output, optionsLoader, exHandler);

		System.exit(cli.executeCommand(args));
	}

}
