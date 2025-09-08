package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import org.junit.jupiter.api.BeforeAll;

class Directory_mixed1_Test extends AbstractDirectoryPrettyPrintTest {

	@BeforeAll
	static void setup() {
		// @formatter:off
		targetPath = FileStructureCreator
			.forTargetPath(root)
				.createFile("file1")
				.createFile("file2")
				.createFile("file3")
				.createAndEnterDirectory("folder4")
					.createFile("file41")
					.createFile("file42")
					.createFile("file43")
				.up()
				.createFile("mFile4")
				.createFile("zFile5")
				.createFile("zFile6")
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
			├─ file2
			├─ file3
			├─ folder4/
			│  ├─ file41
			│  ├─ file42
			│  └─ file43
			├─ mFile4
			├─ zFile5
			└─ zFile6""";
	}

	@Override
	String withEmojiExpected() {
		return """
			📂 targetPath/
			├─ 📄 file1
			├─ 📄 file2
			├─ 📄 file3
			├─ 📂 folder4/
			│  ├─ 📄 file41
			│  ├─ 📄 file42
			│  └─ 📄 file43
			├─ 📄 mFile4
			├─ 📄 zFile5
			└─ 📄 zFile6""";
	}

	@Override
	String withTreeFormatClassicAsciiExpected() {
		return """
			targetPath/
			|-- file1
			|-- file2
			|-- file3
			|-- folder4/
			|   |-- file41
			|   |-- file42
			|   `-- file43
			|-- mFile4
			|-- zFile5
			`-- zFile6""";
	}

	@Override
	String withLimit0Expected() {
		return """
			targetPath/
			└─ ... (6 files and 1 directory skipped)""";
	}

	@Override
	String withLimit1Expected() {
		return """
			targetPath/
			├─ file1
			└─ ... (5 files and 1 directory skipped)""";
	}

	@Override
	String withLimit2Expected() {
		return """
			targetPath/
			├─ file1
			├─ file2
			└─ ... (4 files and 1 directory skipped)""";
	}

	@Override
	String withLimit3Expected() {
		return """
			targetPath/
			├─ file1
			├─ file2
			├─ file3
			└─ ... (3 files and 1 directory skipped)""";
	}

	@Override
	String withCompactDirectoriesExpected() {
		return """
			targetPath/
			├─ file1
			├─ file2
			├─ file3
			├─ folder4/
			│  ├─ file41
			│  ├─ file42
			│  └─ file43
			├─ mFile4
			├─ zFile5
			└─ zFile6""";
	}

}
