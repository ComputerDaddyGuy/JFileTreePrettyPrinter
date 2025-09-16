package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions;

public class Sorting {

	public static void main(String[] args) {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withFileSort(PrettyPrintOptions.Sorts.ALPHABETICAL_ORDER.reversed()))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/file_sort");
		System.out.println(tree);
	}

}
