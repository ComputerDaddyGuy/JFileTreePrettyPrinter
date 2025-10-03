package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathMatchers;
import java.nio.file.Path;
import java.util.function.Function;

public class LineExtension {

	public static void main(String[] args) {
		var printedPath = Path.of("src/example/resources/line_extension");

		Function<Path, String> lineExtension = path -> {
			if (PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/api").matches(path)) {
				return "\t\t\t// All API code: controllers, etc.";
			}
			if (PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/domain").matches(path)) {
				return "\t\t\t// All domain code: value objects, etc.";
			}
			if (PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/infra").matches(path)) {
				return "\t\t\t// All infra code: database, email service, etc.";
			}
			if (PathMatchers.hasNameMatchingGlob("*.properties").matches(path)) {
				return "\t// Config file";
			}
			return null;
		};
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withLineExtension(lineExtension))
			.build();
		var tree = prettyPrinter.prettyPrint(printedPath);
		System.out.println(tree);
	}

}
