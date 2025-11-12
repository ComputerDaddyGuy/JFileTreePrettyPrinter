package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.TreeFormats;

public class FileTreeFormat {

	public static void main(String[] args) {
		System.out.println(run());
	}

	public static String run() {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withTreeFormat(TreeFormats.CLASSIC_ASCII))
			.build();
		return prettyPrinter.prettyPrint("src/main/resources/tree_format");
	}

}
