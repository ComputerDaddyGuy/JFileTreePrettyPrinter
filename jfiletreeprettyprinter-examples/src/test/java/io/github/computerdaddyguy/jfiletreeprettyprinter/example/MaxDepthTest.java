package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaxDepthTest {

	@Test
	void test() {
		var result = MaxDepth.run();

		var expected = """
			max_depth/
			└─ level1/
			   ├─ file1#1
			   ├─ file1#2
			   └─ level2/
			      ├─ file2#1
			      ├─ file2#2
			      └─ level3/
			         └─ ...""";

		assertThat(result).isEqualTo(expected);
	}

}