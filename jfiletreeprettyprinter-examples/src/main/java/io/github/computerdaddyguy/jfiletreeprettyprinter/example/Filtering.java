package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;

public class Filtering {

	public static void main(String[] args) {
		var excludeDirWithNoJavaFiles = PathMatchers.not(PathMatchers.hasNameEndingWith("no_java_file"));
		var hasJavaExtension = PathMatchers.hasExtension("java");

		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(
				options -> options
					.filterDirectories(excludeDirWithNoJavaFiles)
					.filterFiles(hasJavaExtension)
			)
			.build();

		var tree = prettyPrinter.prettyPrint("src/main/resources/filtering");
		System.out.println(tree);
	}

}
