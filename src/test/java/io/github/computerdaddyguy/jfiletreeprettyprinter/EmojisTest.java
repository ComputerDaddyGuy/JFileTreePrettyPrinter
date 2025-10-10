package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.EmojiMapping;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructureCreator;
import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class EmojisTest {

	@TempDir
	private Path root;

	@Test
	void emptyDir() {

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(PrettyPrintOptions::withDefaultEmojis)
			.build();

		var path = FileStructures.emptyDirectory(root);
		var result = printer.prettyPrint(path);
		var expected = "📂 targetPath/";
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void emojis() {

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(PrettyPrintOptions::withDefaultEmojis)
			.build();

		var result = printer.prettyPrint("src/example/resources/emojis");
		var expected = """
			📂 emojis/
			├─ 📂 applications/
			│  ├─ 📂 executables/
			│  │  ├─ 📱 file.apk
			│  │  ├─ 🖥️ file.app
			│  │  ├─ ⚙️ file.bin
			│  │  ├─ ⚙️ file.exe
			│  │  ├─ 📦 file.msi
			│  │  └─ 📱 file2.ipa
			│  └─ 📂 libs/
			│     ├─ 🧩 file.dll
			│     ├─ 🧩 file.lib
			│     └─ 🧩 file.so
			├─ 📂 archives/
			│  ├─ 📦 file.7z
			│  ├─ 📦 file.gz
			│  ├─ 💿 file.img
			│  ├─ 💿 file.iso
			│  ├─ 📦 file.rar
			│  ├─ 📦 file.tar
			│  └─ 📦 file.zip
			├─ 📂 code/
			│  ├─ 🚫 .gitignore
			│  ├─ 🐳 Dockerfile
			│  ├─ 🤵 Jenkinsfile
			│  ├─ 📂 build_tools/
			│  │  ├─ 🛠️ build.gradle
			│  │  ├─ 🛠️ makefile
			│  │  ├─ 🛠️ package.json
			│  │  └─ 🛠️ pom.xml
			│  ├─ ⚙️ docker-compose.yml
			│  ├─ 📂 lang/
			│  │  ├─ 💠 file.c
			│  │  ├─ ☕ file.class
			│  │  ├─ 💠 file.cpp
			│  │  ├─ 💠 file.cs
			│  │  ├─ 🎨 file.css
			│  │  ├─ 🌐 file.htm
			│  │  ├─ 🌐 file.html
			│  │  ├─ 🌐 file.htmx
			│  │  ├─ 📦 file.jar
			│  │  ├─ ☕ file.java
			│  │  ├─ 🎨 file.less
			│  │  ├─ 🐘 file.php
			│  │  ├─ 🐍 file.py
			│  │  ├─ 🎨 file.scss
			│  │  ├─ 🗄️ file.sql
			│  │  └─ 🔷 file.ts
			│  └─ 📂 scripting/
			│     ├─ 📜 file.bash
			│     ├─ 📜 file.bat
			│     └─ 📜 file.sh
			├─ 📂 data/
			│  ├─ ⚙️ file.cfg
			│  ├─ ⚙️ file.conf
			│  ├─ 📊 file.csv
			│  ├─ ⚙️ file.ini
			│  ├─ 📝 file.json
			│  ├─ 📊 file.ods
			│  ├─ ⚙️ file.properties
			│  ├─ 📊 file.xls
			│  ├─ 📊 file.xlsx
			│  ├─ 📝 file.xml
			│  ├─ 📝 file.yaml
			│  └─ 📝 file.yml
			├─ 📂 doc/
			│  ├─ 📝 file.doc
			│  ├─ 📝 file.docx
			│  ├─ 📚 file.epub
			│  ├─ 📝 file.md
			│  ├─ 📝 file.odt
			│  ├─ 📕 file.pdf
			│  ├─ 📝 file.rtf
			│  └─ 📝 file.txt
			├─ 📄 file.unknown_extension
			├─ 📄 file_without_extension
			├─ 📂 internet/
			│  ├─ 📂 github/
			│  │  ├─ 🆕 changelog
			│  │  ├─ 🆕 changelog.md
			│  │  ├─ 🤝 contributing
			│  │  ├─ 🤝 contributing.md
			│  │  ├─ ⚖️ license
			│  │  ├─ ⚖️ license.md
			│  │  ├─ 📘 readme
			│  │  ├─ 📘 readme.md
			│  │  ├─ 🗺️ roadmap
			│  │  ├─ 🗺️ roadmap.md
			│  │  ├─ 🛡️ security
			│  │  └─ 🛡️ security.md
			│  └─ 🤖 robots.txt
			├─ 📂 media/
			│  ├─ 📂 audio/
			│  │  ├─ 🎵 file.aac
			│  │  ├─ 🎵 file.flac
			│  │  ├─ 🎹 file.midi
			│  │  ├─ 🎵 file.mp3
			│  │  ├─ 🎵 file.ogg
			│  │  └─ 🎵 file.wav
			│  ├─ 📂 images/
			│  │  ├─ 🖼️ file.bmp
			│  │  ├─ 🎞️ file.gif
			│  │  ├─ 🖼️ file.ico
			│  │  ├─ 🖼️ file.jpeg
			│  │  ├─ 🖼️ file.jpg
			│  │  ├─ 🖼️ file.png
			│  │  └─ ✒️ file.svg
			│  └─ 📂 video/
			│     ├─ 🎬 file.avi
			│     ├─ 🎬 file.mkv
			│     ├─ 🎬 file.mov
			│     ├─ 🎬 file.mp4
			│     ├─ 🎬 file.webm
			│     └─ 🎬 file.wmv
			└─ 📂 system/
			   ├─ 💾 file.bak
			   ├─ 🔐 file.crt
			   ├─ 🔑 file.key
			   ├─ 📜 file.log
			   ├─ 🔐 file.pem
			   ├─ 🔓 file.pub
			   └─ 🗑️ file.tmp""";

		assertThat(result).isEqualTo(expected);
	}

	@Nested
	class DirectoryEmojiMapping {

		@Test
		void dir_name() {

			// @formatter:off
			var path = FileStructureCreator
				.forTargetPath(root)
				.createDirectory("dirA")
				.createDirectory("dirB")
				.createDirectory("dirC")
				.getPath();
			// @formatter:on

			var mapping = EmojiMapping.builderFromDefault()
				.setDirectoryNameEmoji("dirA", "⭐") // add emoji 
				.build();

			var printer = FileTreePrettyPrinter.builder()
				.customizeOptions(
					options -> options.withEmojis(mapping)
				)
				.build();

			var result = printer.prettyPrint(path);

			var expected = """
				📂 targetPath/
				├─ ⭐ dirA/
				├─ 📂 dirB/
				└─ 📂 dirC/""";

			assertThat(result).isEqualTo(expected);
		}

		@Test
		void dir_match() {

			// @formatter:off
			var path = FileStructureCreator
				.forTargetPath(root)
				.createDirectory("dirA")
				.createDirectory("dirB")
				.createDirectory("dirC")
				.getPath();
			// @formatter:on

			var mapping = EmojiMapping.builderFromDefault()
				.addDirectoryEmoji(PathMatchers.hasName("dirA"), "⭐") // add emoji
				.addDirectoryEmoji(PathMatchers.hasName("dirB"), "😊") // change existing emoji
				.build();

			var printer = FileTreePrettyPrinter.builder()
				.customizeOptions(
					options -> options.withEmojis(mapping)
				)
				.build();

			var result = printer.prettyPrint(path);

			var expected = """
				📂 targetPath/
				├─ ⭐ dirA/
				├─ 😊 dirB/
				└─ 📂 dirC/""";

			assertThat(result).isEqualTo(expected);
		}

	}

	@Nested
	class FileEmojiMapping {

		@Test
		void file_name() {

			// @formatter:off
			var path = FileStructureCreator
				.forTargetPath(root)
				.createFile("aaa") 
				.createFile("dockerfile") 
				.createFile("jenkinsfile") 
				.createFile("license") 
				.getPath();
			// @formatter:on

			var mapping = EmojiMapping.builderFromDefault()
				.setFileNameEmoji("aaa", "⭐") // add emoji 
				.setFileNameEmoji("dockerfile", "😊") // change existing emoji
				.build();

			var printer = FileTreePrettyPrinter.builder()
				.customizeOptions(
					options -> options.withEmojis(mapping)
				)
				.build();

			var result = printer.prettyPrint(path);

			var expected = """
				📂 targetPath/
				├─ ⭐ aaa
				├─ 😊 dockerfile
				├─ 🤵 jenkinsfile
				└─ ⚖️ license""";

			assertThat(result).isEqualTo(expected);
		}

		@Test
		void file_extension() {

			// @formatter:off
			var path = FileStructureCreator
				.forTargetPath(root)
				.createFile("file.plop") 
				.createFile("file.avi") 
				.createFile("file.gif") 
				.createFile("license") 
				.getPath();
			// @formatter:on

			var mapping = EmojiMapping.builderFromDefault()
				.setFileExtensionEmoji("plop", "⭐") // add emoji
				.setFileExtensionEmoji("avi", "😊") // change existing emoji
				.build();

			var printer = FileTreePrettyPrinter.builder()
				.customizeOptions(
					options -> options.withEmojis(mapping)
				)
				.build();

			var result = printer.prettyPrint(path);

			var expected = """
				📂 targetPath/
				├─ 😊 file.avi
				├─ 🎞️ file.gif
				├─ ⭐ file.plop
				└─ ⚖️ license""";

			assertThat(result).isEqualTo(expected);
		}

		@Test
		void file_match() {

			// @formatter:off
			var path = FileStructureCreator
				.forTargetPath(root)
				.createFile("file.plop") 
				.createFile("file.avi") 
				.createFile("file.gif") 
				.createFile("license") 
				.getPath();
			// @formatter:on

			var mapping = EmojiMapping.builderFromDefault()
				.addFileEmoji(PathMatchers.hasName("file.plop"), "⭐") // add emoji
				.addFileEmoji(PathMatchers.hasName("file.avi"), "😊") // change existing emoji
				.build();

			var printer = FileTreePrettyPrinter.builder()
				.customizeOptions(
					options -> options.withEmojis(mapping)
				)
				.build();

			var result = printer.prettyPrint(path);

			var expected = """
				📂 targetPath/
				├─ 😊 file.avi
				├─ 🎞️ file.gif
				├─ ⭐ file.plop
				└─ ⚖️ license""";

			assertThat(result).isEqualTo(expected);
		}

	}

}
