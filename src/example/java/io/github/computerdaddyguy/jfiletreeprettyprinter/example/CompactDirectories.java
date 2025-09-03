package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class CompactDirectories {

	public static void main(String[] args) {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withCompactDirectories(true))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/single_directory_child");
		System.out.println(tree);
	}

}
