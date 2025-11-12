package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class BasicUsage {

	public static void main(String[] args) {
		System.out.println(run());
	}

	public static String run() {
		var prettyPrinter = FileTreePrettyPrinter.createDefault();
		return prettyPrinter.prettyPrint("src/main/resources/base");
	}

}
