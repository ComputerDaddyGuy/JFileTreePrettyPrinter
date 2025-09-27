package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathPredicates;
import java.nio.file.Path;
import java.util.function.Function;

public class LineExtension {

	public static void main(String[] args) {
		Function<Path, String> lineExtension = path -> {
			if (PathPredicates.isDirectory(path) && PathPredicates.hasName(path, "api")) {
				return "\t\t\t// All API code: controllers, etc.";
			}
			if (PathPredicates.isDirectory(path) && PathPredicates.hasName(path, "domain")) {
				return "\t\t\t// All domain code: value objects, etc.";
			}
			if (PathPredicates.isDirectory(path) && PathPredicates.hasName(path, "infra")) {
				return "\t\t\t// All infra code: database, email service, etc.";
			}
			if (PathPredicates.isFile(path) && PathPredicates.hasName(path, "application.properties")) {
				return "\t// Config file";
			}
			return null;
		};
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withLineExtension(lineExtension))
			.build();
		var tree = prettyPrinter.prettyPrint("src/example/resources/line_extension");
		System.out.println(tree);
	}

}
