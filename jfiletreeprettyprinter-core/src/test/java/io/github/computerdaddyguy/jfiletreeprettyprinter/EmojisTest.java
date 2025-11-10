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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

	@ParameterizedTest
	@CsvSource(delimiter = ' ', textBlock = """

		📄 file.unknown_extension
		📄 file_without_extension

		# ------------------------------
		# Application

		# Executables
		📱 file.apk
		🖥️ file.app
		⚙️ file.bin
		⚙️ file.exe
		📦 file.msi
		📱 file.ipa

		# Libs
		🧩 file.dll
		🧩 file.lib
		🧩 file.so

		# ------------------------------
		# Archives

		📦 file.7z
		📦 file.gz
		💿 file.img
		💿 file.iso
		📦 file.rar
		📦 file.tar
		📦 file.zip

		# ------------------------------
		# Code

		🚫 .gitignore
		🐳 Dockerfile
		🤵 Jenkinsfile
		⚙️ docker-compose.yml

		# Build tools
		🛠️ build.gradle
		🛠️ makefile
		🛠️ package.json
		🛠️ pom.xml

		# Lang
		💠 file.c
		☕ file.class
		💠 file.cpp
		💠 file.cs
		🎨 file.css
		🌐 file.htm
		🌐 file.html
		🌐 file.htmx
		📦 file.jar
		☕ file.java
		🎨 file.less
		🐘 file.php
		🐍 file.py
		🎨 file.scss
		🗄️ file.sql
		🔷 file.ts

		# Scripting
		📜 file.bash
		📜 file.bat
		📜 file.sh

		# ------------------------------
		# Data

		⚙️ file.cfg
		⚙️ file.conf
		📊 file.csv
		⚙️ file.ini
		📝 file.json
		📊 file.ods
		⚙️ file.properties
		📊 file.xls
		📊 file.xlsx
		📝 file.xml
		📝 file.yaml
		📝 file.yml

		# ------------------------------
		# Doc

		📝 file.doc
		📝 file.docx
		📚 file.epub
		📝 file.md
		📝 file.odt
		📕 file.pdf
		📝 file.rtf
		📝 file.txt

		# ------------------------------
		# Internet

		🤖 robots.txt

		# Github
		🆕 changelog
		🆕 changelog.md
		🤝 contributing
		🤝 contributing.md
		⚖️ license
		⚖️ license.md
		📘 readme
		📘 readme.md
		🗺️ roadmap
		🗺️ roadmap.md
		🛡️ security
		🛡️ security.md

		# ------------------------------
		# Media

		# Audio
		🎵 file.aac
		🎵 file.flac
		🎹 file.midi
		🎵 file.mp3
		🎵 file.ogg
		🎵 file.wav

		# Images
		🖼️ file.bmp
		🎞️ file.gif
		🖼️ file.ico
		🖼️ file.jpeg
		🖼️ file.jpg
		🖼️ file.png
		✒️ file.svg

		# Video
		🎬 file.avi
		🎬 file.mkv
		🎬 file.mov
		🎬 file.mp4
		🎬 file.webm
		🎬 file.wmv

		# ------------------------------
		# System

		💾 file.bak
		🔐 file.crt
		🔑 file.key
		📜 file.log
		🔐 file.pem
		🔓 file.pub
		🗑️ file.tmp

		""")
	void emojis(String expectedEmoji, String fileName) {

		var printer = FileTreePrettyPrinter.builder()
			.customizeOptions(PrettyPrintOptions::withDefaultEmojis)
			.build();

		var path = FileStructureCreator.forTargetPath(root)
			.createFile(fileName)
			.getPath();

		var result = printer.prettyPrint(path);

		var expected = "📂 targetPath/\n└─ " + expectedEmoji + " " + fileName;

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
