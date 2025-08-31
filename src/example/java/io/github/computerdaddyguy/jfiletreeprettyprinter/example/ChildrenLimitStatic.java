package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class ChildrenLimitStatic {

	public static void main(String[] args) {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildrenLimit(3))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/children_limit_static");
		System.out.println(tree);
	}

}
