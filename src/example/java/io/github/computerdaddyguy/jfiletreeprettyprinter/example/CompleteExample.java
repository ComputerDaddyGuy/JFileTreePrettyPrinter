package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.ChildLimitBuilder;
import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathPredicates;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions.Sorts;
import java.nio.file.Path;
import java.util.function.Function;

public class CompleteExample {

	public static void main(String[] args) {

		var rootFolder = "JFileTreePrettyPrinter";

		var filterDir = PathPredicates.builder()
			.pathTest(path -> !PathPredicates.hasName(path, ".git"))
			.pathTest(path -> !PathPredicates.hasFullPathMatchingGlob(path, "**/.git"))
			.pathTest(path -> !PathPredicates.hasFullPathMatchingGlob(path, "**/.github"))
			.pathTest(path -> !PathPredicates.hasFullPathMatchingGlob(path, "**/.settings"))
			.pathTest(path -> !PathPredicates.hasFullPathMatchingGlob(path, "**/src/example"))
			.pathTest(path -> !PathPredicates.hasFullPathMatchingGlob(path, "**/src/test"))
			.pathTest(path -> !PathPredicates.hasFullPathMatchingGlob(path, "**/target"))
			.build();

		var filterFiles = PathPredicates.builder()
			.pathTest(path -> !PathPredicates.hasNameStartingWith(path, "."))
			.pathTest(path -> {
				if (PathPredicates.hasParentMatching(path, parent -> PathPredicates.hasName(parent, "jfiletreeprettyprinter"))) {
					return PathPredicates.hasName(path, "FileTreePrettyPrinter.java");
				}
				return true;
			})
			.build();

		var childLimitFunction = ChildLimitBuilder.builder()
			.limit(path -> PathPredicates.hasFullPathMatchingGlob(path, "**/io/github/computerdaddyguy/jfiletreeprettyprinter/renderer"), 0)
			.limit(path -> PathPredicates.hasFullPathMatchingGlob(path, "**/io/github/computerdaddyguy/jfiletreeprettyprinter/scanner"), 0)
			.build();

		Function<Path, String> lineExtension = path -> {
			if (PathPredicates.hasName(path, "JfileTreePrettyPrinter-structure.png")) {
				return "\t// This image";
			} else if (PathPredicates.hasName(path, "FileTreePrettyPrinter.java")) {
				return "\t// Main entry point";
			} else if (PathPredicates.hasName(path, "README.md")) {
				return "\t\t// You're reading at this!";
			} else if (PathPredicates.hasName(path, "java")) {
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
		var tree = prettyPrinter.prettyPrint(".");
		System.out.println(tree);
	}

}
