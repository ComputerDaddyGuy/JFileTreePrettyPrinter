package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FilteringTest {

	@Test
	void test() {
		var result = Filtering.run();

		var expected = """
			filtering/
			├─ dir_with_java_files/
			│  ├─ file_B.java
			│  └─ file_E.java
			├─ dir_with_nested_java_files/
			│  └─ nested_dir_with_java_files/
			│     ├─ file_G.java
			│     └─ file_J.java
			└─ file_A.java""";

		assertThat(result).isEqualTo(expected);
	}

}