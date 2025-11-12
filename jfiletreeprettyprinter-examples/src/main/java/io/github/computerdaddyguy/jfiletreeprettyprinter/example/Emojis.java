package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;

public class Emojis {

	public static void main(String[] args) {
		System.out.println(run());
	}

	public static String run() {
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(options -> options.withDefaultEmojis()) // or withEmojis(EmojiMapping) for custom mapping
			.build();
		return prettyPrinter.prettyPrint("src/main/resources/emojis");
	}

}
