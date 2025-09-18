package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions;

public class Sorting {

	public static void main(String[] args) {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(PrettyPrintOptions.Sorts.DIRECTORY_FIRST))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/sorting");
		System.out.println(tree);
	}

}
