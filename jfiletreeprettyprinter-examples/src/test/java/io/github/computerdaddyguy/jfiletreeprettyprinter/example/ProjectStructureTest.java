package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProjectStructureTest {

	@Test
	void test() {
		var result = ProjectStructure.run();

		var expected = """
			📂 JFileTreePrettyPrinter/
			├─ 📂 jfiletreeprettyprinter-core/	// The Java lib
			│  ├─ 📂 src/main/java/io/github/computerdaddyguy/jfiletreeprettyprinter/
			│  │  └─ ☕ FileTreePrettyPrinter.java	// Lib main entry point
			│  └─ ...
			├─ 📂 jfiletreeprettyprinter-examples/	// Some examples
			│  └─ ...
			├─ 📂 jfiletreeprettyprinter-cli/	// Everything to build the executable
			│  └─ ...
			├─ 📂 assets/
			│  └─ 🖼️ project-structure.png	// This image
			├─ 📂 docs/
			│  ├─ 📝 How-to-build-a-native-executable-locally.md
			│  └─ 📝 Release-process.md
			├─ 🆕 CHANGELOG.md
			├─ 🤝 CONTRIBUTING.md
			├─ ⚖️ LICENSE
			├─ 📘 README.md		// You're reading at this!
			├─ 🗺️ ROADMAP.md
			├─ 🛡️ SECURITY.md
			├─ 🛠️ pom.xml
			└─ 📜 runMutationTests.sh""";

		assertThat(result).isEqualTo(expected);
	}

}