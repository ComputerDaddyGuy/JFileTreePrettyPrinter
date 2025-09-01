package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;

class Directory_mixed2_Test extends AbstractDirectoryPrettyPrintTest {

	@BeforeAll
	static void setup() throws IOException {

		// @formatter:off
		targetPath = FileStructureCreator
			.forTargetPath(root)
				.createFile("file1")
				.createFile("file2")
				.createFile("file3")
				.createDirectory("folder4")
					.createFile("file41")
					.createFile("file42")
					.createFile("file43")
				.end()
				.createFile("mFile4")
				.createDirectory("mFolder5")
					.createFile("file51")
					.createFile("file52")
					.createFile("file53")
				.end()
				.createFile("zFile5")
				.createFile("zFile6")
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
			├─ folder4/
			│  ├─ file41
			│  ├─ file42
			│  └─ file43
			├─ mFile4
			├─ mFolder5/
			│  ├─ file51
			│  ├─ file52
			│  └─ file53
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
			├─ 📂 mFolder5/
			│  ├─ 📄 file51
			│  ├─ 📄 file52
			│  └─ 📄 file53
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
			|-- mFolder5/
			|   |-- file51
			|   |-- file52
			|   `-- file53
			|-- zFile5
			`-- zFile6""";
	}

	@Override
	String withLimit0Expected() {
		return """
			targetPath/
			└─ ... (6 files and 2 directories skipped)""";
	}

	@Override
	String withLimit1Expected() {
		return """
			targetPath/
			├─ file1
			└─ ... (5 files and 2 directories skipped)""";
	}

	@Override
	String withLimit2Expected() {
		return """
			targetPath/
			├─ file1
			├─ file2
			└─ ... (4 files and 2 directories skipped)""";
	}

	@Override
	String withLimit3Expected() {
		return """
			targetPath/
			├─ file1
			├─ file2
			├─ file3
			└─ ... (3 files and 2 directories skipped)""";
	}

}
