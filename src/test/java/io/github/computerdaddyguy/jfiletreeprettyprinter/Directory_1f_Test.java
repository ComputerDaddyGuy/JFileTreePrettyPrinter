package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;

class Directory_1f_Test extends AbstractDirectoryPrettyPrintTest {

	@BeforeAll
	static void setup() throws IOException {
		// @formatter:off
		targetPath = FileStructureCreator
			.forTargetPath(root)
				.createFile("file1")
			.end()
			.getCurrentDir()
			;
		// @formatter:on
	}

	@Override
	String defaultOptionsExpected() {
		return """
			targetPath/
			└─ file1""";
	}

	@Override
	String withEmojiExpected() {
		return """
			📂 targetPath/
			└─ file1""";
	}

	@Override
	String withDepthFormatClassicAsciiExpected() {
		return """
			targetPath/
			`-- file1""";
	}

	@Override
	String withLimit0Expected() {
		return """
			targetPath/
			└─ file1""";
	}

	@Override
	String withLimit1Expected() {
		return """
			targetPath/
			└─ file1""";
	}

	@Override
	String withLimit2Expected() {
		return """
			targetPath/
			└─ file1""";
	}

	@Override
	String withLimit3Expected() {
		return """
			targetPath/
			└─ file1""";
	}

}
