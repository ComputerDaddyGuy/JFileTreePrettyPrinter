package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathSorts;

public class Sorting {

	public static void main(String[] args) {
		System.out.println(run());
	}

	public static String run() {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.sort(PathSorts.DIRECTORY_FIRST))
			.build();
		return prettyPrinter.prettyPrint("src/main/resources/sorting");
	}

}
