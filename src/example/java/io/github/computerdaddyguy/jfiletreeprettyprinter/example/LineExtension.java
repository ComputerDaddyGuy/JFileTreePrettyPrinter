package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathUtils;
import java.nio.file.Path;
import java.util.function.Function;

public class LineExtension {

	public static void main(String[] args) {
		Function<Path, String> lineExtension = path -> {
			if (PathUtils.isDirectory(path) && PathUtils.hasName(path, "api")) {
				return "\t\t\t// All API code: controllers, etc.";
			}
			if (PathUtils.isDirectory(path) && PathUtils.hasName(path, "domain")) {
				return "\t\t\t// All domain code: value objects, etc.";
			}
			if (PathUtils.isDirectory(path) && PathUtils.hasName(path, "infra")) {
				return "\t\t\t// All infra code: database, email service, etc.";
			}
			if (PathUtils.isFile(path) && PathUtils.hasName(path, "application.properties")) {
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
