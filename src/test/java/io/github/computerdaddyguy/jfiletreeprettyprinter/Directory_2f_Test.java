package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import org.junit.jupiter.api.BeforeAll;

class Directory_2f_Test extends AbstractDirectoryPrettyPrintTest {

	@BeforeAll
	static void setup() {
		// @formatter:off
		targetPath = FileStructureCreator
			.forTargetPath(root)
				.createFile("file1")
				.createFile("file2")
			.up()
			.getPath()
			;
		// @formatter:on
	}

	@Override
	String defaultOptionsExpected() {
		return """
			targetPath/
			├─ file1
			└─ file2""";
	}

	@Override
	String withEmojiExpected() {
		return """
			📂 targetPath/
			├─ 📄 file1
			└─ 📄 file2""";
	}

	@Override
	String withTreeFormatClassicAsciiExpected() {
		return """
			targetPath/
			|-- file1
			`-- file2""";
	}

	@Override
	String withLimit0Expected() {
		return """
			targetPath/
			└─ ... (2 files skipped)""";
	}

	@Override
	String withLimit1Expected() {
		return """
			targetPath/
			├─ file1
			└─ ... (1 file skipped)""";
	}

	@Override
	String withLimit2Expected() {
		return """
			targetPath/
			├─ file1
			└─ file2""";
	}

	@Override
	String withLimit3Expected() {
		return """
			targetPath/
			├─ file1
			└─ file2""";
	}

	@Override
	String withCompactDirectoriesExpected() {
		return """
			targetPath/
			├─ file1
			└─ file2""";
	}

}
