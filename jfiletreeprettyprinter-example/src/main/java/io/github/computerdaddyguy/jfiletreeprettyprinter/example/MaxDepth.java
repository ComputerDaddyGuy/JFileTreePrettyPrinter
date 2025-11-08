package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class MaxDepth {

	public static void main(String[] args) {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withMaxDepth(3))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/max_depth");
		System.out.println(tree);
	}

}
