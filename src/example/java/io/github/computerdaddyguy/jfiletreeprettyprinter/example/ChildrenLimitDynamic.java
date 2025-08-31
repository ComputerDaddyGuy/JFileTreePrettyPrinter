package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathPredicates;
import java.nio.file.Path;
import java.util.function.Function;

public class ChildrenLimitDynamic {

	public static void main(String[] args) {
		Function<Path, Integer> pathLimitFunction = path -> PathPredicates.hasName(path, "node_modules") ? 0 : -1; // Negative value means "no limit"
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withChildrenLimitFunction(pathLimitFunction))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/children_limit_dynamic");
		System.out.println(tree);
	}

}
