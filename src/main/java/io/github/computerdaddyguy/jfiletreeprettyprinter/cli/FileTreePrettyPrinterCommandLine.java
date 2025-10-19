package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import picocli.CommandLine;

public class FileTreePrettyPrinterCommandLine {

	public static void main(String[] args) {
		int exitCode = new CommandLine(new PrettyPrintCommand()).execute(args);
		System.exit(exitCode);
	}

}
