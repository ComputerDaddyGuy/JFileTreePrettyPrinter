package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class Emojis {

	public static void main(String[] args) {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withEmojis(true))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/emojis");
		System.out.println(tree);
	}

}
