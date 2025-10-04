package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.ChildLimits;
import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathMatchers;

public class ChildLimitDynamic {

	public static void main(String[] args) {
		var childLimit = ChildLimits.builder()
			.setDefault(ChildLimits.UNLIMITED) // Unlimited children by default
			.add(PathMatchers.hasName("node_modules"), 0) // Do NOT print any children in "node_modules" folder
			.build();
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(childLimit))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/child_limit_dynamic");
		System.out.println(tree);
	}

}
