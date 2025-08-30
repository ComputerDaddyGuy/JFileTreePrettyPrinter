package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import org.junit.jupiter.api.BeforeAll;

class Directory_empty_Test extends AbstractDirectoryPrettyPrintTest {

	@BeforeAll
	static void setup() {
		// @formatter:off
		targetPath = FileStructureCreator
			.forTargetPath(root)
			.end()
			.getCurrentDir()
			;
		// @formatter:on
	}

	@Override
	String defaultOptionsExpected() {
		return "targetPath/";
	}

	@Override
	String withEmojiExpected() {
		return "📂 targetPath/";
	}

	@Override
	String withDepthFormatClassicAsciiExpected() {
		return "targetPath/";
	}

	@Override
	String withLimit0Expected() {
		return "targetPath/";
	}

	@Override
	String withLimit1Expected() {
		return "targetPath/";
	}

	@Override
	String withLimit2Expected() {
		return "targetPath/";
	}

	@Override
	String withLimit3Expected() {
		return "targetPath/";
	}

}
