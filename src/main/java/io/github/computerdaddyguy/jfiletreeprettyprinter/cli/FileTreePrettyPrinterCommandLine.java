package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.ExternalOptionsMapper;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.ExternalOptionsReader;
import picocli.CommandLine;

class FileTreePrettyPrinterCommandLine {

	public static void main(String[] args) {
		System.exit(executeCommand(args));
	}

	static int executeCommand(String[] args) {
		var reader = ExternalOptionsReader.createDefault();
		var mapper = ExternalOptionsMapper.createDefault();

		var cmd = new CommandLine(new PrettyPrintCommand(reader, mapper));

		return cmd.execute(args);
	}

}
