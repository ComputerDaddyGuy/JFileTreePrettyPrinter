package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FileTreeFormatTest {

	@Test
	void test() {
		var result = FileTreeFormat.run();

		var expected = """
			tree_format/
			|-- file_1
			|-- file_2
			`-- subFolder/
			    |-- subFile_1
			    `-- subFile_2""";

		assertThat(result).isEqualTo(expected);
	}

}