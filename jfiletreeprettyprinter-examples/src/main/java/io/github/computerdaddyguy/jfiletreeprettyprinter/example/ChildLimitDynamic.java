package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.ChildLimits;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;

public class ChildLimitDynamic {

	public static void main(String[] args) {
		System.out.println(run());
	}

	public static String run() {
		var childLimit = ChildLimits.builder()
			.setDefault(ChildLimits.UNLIMITED) // Unlimited children by default
			.add(PathMatchers.hasName("node_modules"), 0) // Do NOT print any children in "node_modules" folder
			.build();
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildLimit(childLimit))
			.build();
		return prettyPrinter.prettyPrint("src/main/resources/child_limit_dynamic");
	}

}
