package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathPredicates;

public class Filtering {

	public static void main(String[] args) {
		var hasJavaExtensionPredicate = PathPredicates.builder().hasExtension("java").build();
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.filter(hasJavaExtensionPredicate))
			.build();

		var tree = prettyPrinter.prettyPrint("src/example/resources/filtering");
		System.out.println(tree);
	}

}
