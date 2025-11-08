package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.LineExtensions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;
import java.nio.file.Path;
import java.util.function.Function;

public class LineExtension {

	public static void main(String[] args) {
		var printedPath = Path.of("src/example/resources/line_extension");

		Function<Path, String> lineExtension = LineExtensions.builder()
			.add(PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/api"), "\t\t\t// All API code: controllers, etc.")
			.add(PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/domain"), "\t\t\t// All domain code: value objects, etc.")
			.add(PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/infra"), "\t\t\t// All infra code: database, email service, etc.")
			.add(PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/api"), "\t\t\t// All API code: controllers, etc.")
			.add(PathMatchers.hasNameMatchingGlob("*.properties"), "\t// Config file")
			.build();

		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withLineExtension(lineExtension))
			.build();
		var tree = prettyPrinter.prettyPrint(printedPath);
		System.out.println(tree);
	}

}
