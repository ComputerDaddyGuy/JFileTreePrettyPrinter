package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import picocli.CommandLine;

class FileTreePrettyPrinterCommandLine {

	public static void main(String[] args) {
		var cmd = new CommandLine(new PrettyPrintCommand());
		int exitCode = cmd.execute(args);
		System.exit(exitCode);
	}

}
