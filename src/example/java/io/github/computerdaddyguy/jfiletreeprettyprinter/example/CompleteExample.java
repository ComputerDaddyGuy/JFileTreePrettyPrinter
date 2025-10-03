package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.ChildLimitBuilder;
import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathMatchers;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions.Sorts;
import java.nio.file.Path;
import java.util.function.Function;

public class CompleteExample {

	public static void main(String[] args) {

		var jFileTreePrettyPrintFolder = Path.of(".");

		var filterDir = PathMatchers.noneOf(
			PathMatchers.hasName(".git"),
			PathMatchers.hasRelativePathMatchingGlob(jFileTreePrettyPrintFolder, ".git"),
			PathMatchers.hasRelativePathMatchingGlob(jFileTreePrettyPrintFolder, ".github"),
			PathMatchers.hasRelativePathMatchingGlob(jFileTreePrettyPrintFolder, ".settings"),
			PathMatchers.hasRelativePathMatchingGlob(jFileTreePrettyPrintFolder, "src/example"),
			PathMatchers.hasRelativePathMatchingGlob(jFileTreePrettyPrintFolder, "src/test"),
			PathMatchers.hasRelativePathMatchingGlob(jFileTreePrettyPrintFolder, "target")
		);

		var filterFiles = PathMatchers.allOf(
			PathMatchers.not(PathMatchers.hasNameStartingWith(".")),
			PathMatchers.ifMatchesThenElse(
				PathMatchers.hasDirectParentMatching(PathMatchers.hasName("jfiletreeprettyprinter")), // if
				PathMatchers.hasName("FileTreePrettyPrinter.java"), // then
				p -> true // else
			)
		);

		var childLimitFunction = ChildLimitBuilder.builder()
			.limit(PathMatchers.hasAbsolutePathMatchingGlob("**/io/github/computerdaddyguy/jfiletreeprettyprinter/renderer"), 0)
			.limit(PathMatchers.hasAbsolutePathMatchingGlob("**/io/github/computerdaddyguy/jfiletreeprettyprinter/scanner"), 0)
			.build();

		Function<Path, String> lineExtension = path -> {
			if (PathMatchers.hasName("JfileTreePrettyPrinter-structure.png").matches(path)) {
				return "\t// This image";
			} else if (PathMatchers.hasName("FileTreePrettyPrinter.java").matches(path)) {
				return "\t// Main entry point";
			} else if (PathMatchers.hasName("README.md").matches(path)) {
				return "\t\t// You're reading at this!";
			} else if (PathMatchers.hasName("java").matches(path)) {
				return "";
			}
			return null;
		};

		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(
				options -> options
					.withEmojis(true)
					.withCompactDirectories(true)
					.filterDirectories(filterDir)
					.filterFiles(filterFiles)
					.withChildLimit(childLimitFunction)
					.withLineExtension(lineExtension)
					.sort(Sorts.DIRECTORY_FIRST)
			)
			.build();

		var tree = prettyPrinter.prettyPrint(jFileTreePrettyPrintFolder);
		System.out.println(tree);
	}

}
