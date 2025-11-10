package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EmojisTest {

	@Test
	void test() {
		var result = Emojis.run();

		var expected = """
			📂 emojis/
			├─ 🐳 Dockerfile
			├─ 🤵 Jenkinsfile
			├─ 🎬 file.avi
			├─ 📝 file.docx
			├─ ⚙️ file.ini
			├─ ☕ file.java
			├─ 🖼️ file.jpeg
			├─ 🎵 file.mp3
			├─ 📕 file.pdf
			├─ 📊 file.xlsx
			├─ 📦 file.zip
			└─ 📘 readme""";

		assertThat(result).isEqualTo(expected);
	}

}
