package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;

class Directory_3f1d_Test extends AbstractDirectoryPrettyPrintTest {

	@BeforeAll
	static void setup() throws IOException {
		// @formatter:off
		targetPath = FileStructureCreator
			.forTargetPath(root)
				.createFile("file1")
				.createFile("file2")
				.createFile("file3")
				.createDirectory("folder4")
					.createFile("file4")
				.end()
			.end()
			.getCurrentDir()
			;
		// @formatter:on
	}

	@Override
	String defaultOptionsExpected() {
		return """
			targetPath/
			├─ file1
			├─ file2
			├─ file3
			└─ folder4/
			   └─ file4""";
	}

	@Override
	String withEmojiExpected() {
		return """
			📂 targetPath/
			├─ file1
			├─ file2
			├─ file3
			└─ 📂 folder4/
			   └─ file4""";
	}

	@Override
	String withTreeFormatClassicAsciiExpected() {
		return """
			targetPath/
			|-- file1
			|-- file2
			|-- file3
			`-- folder4/
			    `-- file4""";
	}

	@Override
	String withLimit0Expected() {
		return """
			targetPath/
			└─ ... (3 files and 1 directory skipped)""";
	}

	@Override
	String withLimit1Expected() {
		return """
			targetPath/
			├─ file1
			└─ ... (2 files and 1 directory skipped)""";
	}

	@Override
	String withLimit2Expected() {
		return """
			targetPath/
			├─ file1
			├─ file2
			└─ ... (1 file and 1 directory skipped)""";
	}

	@Override
	String withLimit3Expected() {
		return """
			targetPath/
			├─ file1
			├─ file2
			├─ file3
			└─ ... (1 directory skipped)""";
	}

}
