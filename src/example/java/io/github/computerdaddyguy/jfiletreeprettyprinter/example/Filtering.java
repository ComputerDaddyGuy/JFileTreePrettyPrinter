package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathPredicates;

public class Filtering {

	public static void main(String[] args) {
		var filter = PathPredicates.hasExtension("java");
		var prettyPrinter = FileTreePrettyPrinter.createDefault();
		var tree = prettyPrinter.prettyPrint("src/example/resources/filtering", filter);
		System.out.println(tree);
	}

}
