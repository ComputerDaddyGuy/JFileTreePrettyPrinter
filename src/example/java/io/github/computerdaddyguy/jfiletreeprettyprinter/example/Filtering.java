package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathPredicates;
import java.nio.file.Path;
import java.util.function.Predicate;

public class Filtering {

	public static void main(String[] args) {
		Predicate<Path> excludeDirWithNoJavaFiles = dir -> !PathPredicates.hasNameEndingWith(dir, "no_java_file");
		var hasJavaExtensionPredicate = PathPredicates.builder().hasExtension("java").build();

		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(
				options -> options
					.filterDirectories(excludeDirWithNoJavaFiles)
					.filterFiles(hasJavaExtensionPredicate)
			)
			.build();

		var tree = prettyPrinter.prettyPrint("src/example/resources/filtering");
		System.out.println(tree);
	}

}
