package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.util.FileStructures;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class EmojisTest {

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
	void emojis() {

		var result = printer.prettyPrint("src/example/resources/emojis");
		var expected = """
			📂 emojis/
			├─ 📂 applications/
			│  ├─ 📂 executables/
			│  │  ├─ 📱 file.apk
			│  │  ├─ 🖥️ file.app
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
			│  ├─ 🙈 .gitignore
			│  ├─ 🐳 Dockerfile
			│  ├─ 🤵 Jenkinsfile
			│  ├─ 📂 build_tools/
			│  │  ├─ 🏗️ build.gradle
			│  │  ├─ 🏗️ package.json
			│  │  └─ 🏗️ pom.xml
			│  ├─ 📂 github/
			│  │  ├─ 🗺️ changelog
			│  │  ├─ 🗺️ changelog.md
			│  │  ├─ 🗺️ licence
			│  │  ├─ 🗺️ licence.md
			│  │  ├─ 📖 readme
			│  │  ├─ 📖 readme.md
			│  │  ├─ 🗺️ roadmap
			│  │  ├─ 🗺️ roadmap.md
			│  │  ├─ 🛡️ security
			│  │  └─ 🛡️ security.md
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
			│  ├─ 📃 file.doc
			│  ├─ 📃 file.docx
			│  ├─ 📚 file.epub
			│  ├─ 📖 file.md
			│  ├─ 📃 file.odt
			│  ├─ 📕 file.pdf
			│  ├─ 📃 file.rtf
			│  └─ 📄 file.txt
			├─ 📄 file.unknown_extension
			├─ 📄 file_without_extension
			├─ 📂 media/
			│  ├─ 📂 audio/
			│  │  ├─ 🎶 file.aac
			│  │  ├─ 🎶 file.flac
			│  │  ├─ 🎹 file.midi
			│  │  ├─ 🎵 file.mp3
			│  │  ├─ 🎶 file.ogg
			│  │  └─ 🎵 file.wav
			│  ├─ 📂 images/
			│  │  ├─ 🖼️ file.bmp
			│  │  ├─ 🎞️ file.gif
			│  │  ├─ 🔲 file.ico
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

}
