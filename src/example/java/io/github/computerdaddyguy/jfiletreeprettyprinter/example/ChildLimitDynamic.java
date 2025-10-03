package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.ChildLimitBuilder;
import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathMatchers;

public class ChildLimitDynamic {

	public static void main(String[] args) {
		var isNodeModuleMatcher = PathMatchers.hasName("node_modules");
		var childLimit = ChildLimitBuilder.builder()
			.defaultLimit(ChildLimitBuilder.UNLIMITED)
			.limit(isNodeModuleMatcher, 0)
			.build();
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(childLimit))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/child_limit_dynamic");
		System.out.println(tree);
	}

}
