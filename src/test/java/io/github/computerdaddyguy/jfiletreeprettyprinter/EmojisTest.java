package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class EmojisTest {

	@TempDir
	private Path root;

	private FileTreePrettyPrinter printer = FileTreePrettyPrinter.builder()
		.customizeOptions(
			options -> options.withEmojis(true)
		)
		.build();

	@Test
	void emptyDir() {
		var path = FileStructures.emptyDirectory(root);
		var result = printer.prettyPrint(path);
		var expected = "📂 targetPath/";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void unknown_extension() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
			.createFile("file_without%extension")
			.createFile("file.unexisting%extension")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ 📄 file.unexisting%extension
			└─ 📄 file_without%extension""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void archives() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createFile("file.7z")
				.createFile("file.gz")
				.createFile("file.img")
				.createFile("file.iso")
				.createFile("file.rar")
				.createFile("file.tar")
				.createFile("file.zip")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ 📦 file.7z
			├─ 📦 file.gz
			├─ 💿 file.img
			├─ 💿 file.iso
			├─ 📦 file.rar
			├─ 📦 file.tar
			└─ 📦 file.zip""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void code() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createFile(".gitignore")
				.createFile("Dockerfile")
				.createFile("Jenkinsfile")
				.createFile("readme")
				.createFile("readme.md")
				.createFile("roadmap")
				.createFile("roadmap.md")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ 🙈 .gitignore
			├─ 🐳 Dockerfile
			├─ 🤵 Jenkinsfile
			├─ 📖 readme
			├─ 📖 readme.md
			├─ 🗺️ roadmap
			└─ 🗺️ roadmap.md""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void code_buildTools() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
			.createFile("build.gradle")
			.createFile("package.json")
			.createFile("pom.xml")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ 🏗️ build.gradle
			├─ 🏗️ package.json
			└─ 🏗️ pom.xml""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void code_lang() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createFile("file.class")
				.createFile("file.jar")
				.createFile("file.java")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ ☕ file.class
			├─ 📦 file.jar
			└─ ☕ file.java""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void code_scripting() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createFile("file.bat")
				.createFile("file.sh")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ 📜 file.bat
			└─ 📜 file.sh""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void data() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createFile("file.cfg")
				.createFile("file.conf")
				.createFile("file.csv")
				.createFile("file.ini")
				.createFile("file.json")
				.createFile("file.ods")
				.createFile("file.properties")
				.createFile("file.xls")
				.createFile("file.xlsx")
				.createFile("file.xml")
				.createFile("file.yaml")
				.createFile("file.yml")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ ⚙️ file.cfg
			├─ ⚙️ file.conf
			├─ 📊 file.csv
			├─ ⚙️ file.ini
			├─ 📝 file.json
			├─ 📊 file.ods
			├─ ⚙️ file.properties
			├─ 📊 file.xls
			├─ 📊 file.xlsx
			├─ 📝 file.xml
			├─ 📝 file.yaml
			└─ 📝 file.yml""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void doc() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createFile("file.doc")
				.createFile("file.docx")
				.createFile("file.epub")
				.createFile("file.md")
				.createFile("file.odt")
				.createFile("file.pdf")
				.createFile("file.rtf")
				.createFile("file.txt")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ 📃 file.doc
			├─ 📃 file.docx
			├─ 📚 file.epub
			├─ 📖 file.md
			├─ 📃 file.odt
			├─ 📕 file.pdf
			├─ 📃 file.rtf
			└─ 📄 file.txt""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void media_audio() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createFile("file.aac")
				.createFile("file.flac")
				.createFile("file.midi")
				.createFile("file.mp3")
				.createFile("file.ogg")
				.createFile("file.wav")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ 🎶 file.aac
			├─ 🎶 file.flac
			├─ 🎹 file.midi
			├─ 🎵 file.mp3
			├─ 🎶 file.ogg
			└─ 🎵 file.wav""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void media_images() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createFile("file.bmp")
				.createFile("file.gif")
				.createFile("file.jpeg")
				.createFile("file.jpg")
				.createFile("file.png")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ 🖼️ file.bmp
			├─ 🎞️ file.gif
			├─ 🖼️ file.jpeg
			├─ 🖼️ file.jpg
			└─ 🖼️ file.png""";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void media_videos() {
		// @formatter:off
		var path = FileStructureCreator
			.forTargetPath(root)
				.createFile("file.avi")
				.createFile("file.mkv")
				.createFile("file.mov")
				.createFile("file.mp3")
				.createFile("file.webm")
				.createFile("file.wmv")
			.getPath()
			;
		// @formatter:on
		var result = printer.prettyPrint(path);
		var expected = """
			📂 targetPath/
			├─ 🎬 file.avi
			├─ 🎬 file.mkv
			├─ 🎬 file.mov
			├─ 🎵 file.mp3
			├─ 🎬 file.webm
			└─ 🎬 file.wmv""";
		assertThat(result).isEqualTo(expected);
	}

}
