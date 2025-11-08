package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class ChildLimitStatic {

	public static void main(String[] args) {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(3))
			.build();
		var tree = prettyPrinter.prettyPrint("src/main/resources/child_limit_static");
		System.out.println(tree);
	}

}
