package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class MaxDepth {

	public static void main(String[] args) {
		System.out.println(run());
	}

	public static String run() {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withMaxDepth(3))
			.build();
		return prettyPrinter.prettyPrint("src/main/resources/max_depth");
	}

}
