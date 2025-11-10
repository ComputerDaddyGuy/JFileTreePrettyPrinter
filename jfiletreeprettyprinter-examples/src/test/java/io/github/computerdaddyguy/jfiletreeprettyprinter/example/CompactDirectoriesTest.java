package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CompactDirectoriesTest {

	@Test
	void test() {
		var result = CompactDirectories.run();

		var expected = """
			single_directory_child/
			├─ file1
			├─ file2
			└─ this/is/single/directory/child/
			   ├─ file1
			   ├─ file2
			   └─ file3""";

		assertThat(result).isEqualTo(expected);
	}

}