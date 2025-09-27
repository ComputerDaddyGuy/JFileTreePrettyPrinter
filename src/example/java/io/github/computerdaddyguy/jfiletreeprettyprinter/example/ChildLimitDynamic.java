package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.ChildLimitBuilder;
import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathPredicates;

public class ChildLimitDynamic {

	public static void main(String[] args) {
		var isNodeModulePredicate = PathPredicates.builder().hasName("node_modules").build();
		var childLimit = ChildLimitBuilder.builder()
			.defaultLimit(ChildLimitBuilder.UNLIMITED)
			.limit(isNodeModulePredicate, 0)
			.build();
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(childLimit))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/child_limit_dynamic");
		System.out.println(tree);
	}

}
