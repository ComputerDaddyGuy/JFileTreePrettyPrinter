package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class FileTreePrettyPrinterCommandLine {

	public static void main(String[] args) {
		String path = ".";
		if (args != null && args.length > 0) {
			path = args[0];
		}
		try {
			var printer = FileTreePrettyPrinter.createDefault();
			var result = printer.prettyPrint(path);
			System.out.println(result);
		} catch (Exception e) {
			System.err.print("Error while pretty printing: " + e.getMessage());
		}
	}

}
