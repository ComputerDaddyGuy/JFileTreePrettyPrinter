package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions.TreeFormat;

public class FileTreeFormat {

	public static void main(String[] args) {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withTreeFormat(TreeFormat.CLASSIC_ASCII))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/tree_format");
		System.out.println(tree);
	}

}
