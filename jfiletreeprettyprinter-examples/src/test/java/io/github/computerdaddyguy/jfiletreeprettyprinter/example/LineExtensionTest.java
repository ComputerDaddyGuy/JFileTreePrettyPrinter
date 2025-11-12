package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LineExtensionTest {

	@Test
	void test() {
		var result = LineExtension.run();

		var expected = """
			line_extension/
			└─ src/
			   └─ main/
			      ├─ java/
			      │  ├─ api/			// All API code: controllers, etc.
			      │  │  └─ Controller.java
			      │  ├─ domain/			// All domain code: value objects, etc.
			      │  │  └─ ValueObject.java
			      │  └─ infra/			// All infra code: database, email service, etc.
			      │     └─ Repository.java
			      └─ resources/
			         └─ application.properties	// Config file""";

		assertThat(result).isEqualTo(expected);
	}

}
