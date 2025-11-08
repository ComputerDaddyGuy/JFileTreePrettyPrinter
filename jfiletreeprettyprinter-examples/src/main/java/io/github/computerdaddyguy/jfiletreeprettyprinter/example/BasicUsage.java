package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class BasicUsage {

	public static void main(String[] args) {
		var prettyPrinter = FileTreePrettyPrinter.createDefault();
		var tree = prettyPrinter.prettyPrint("src/main/resources/base");
		System.out.println(tree);
	}

}
