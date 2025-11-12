package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ChildLimitStaticTest {

	@Test
	void test() {
		var result = ChildLimitStatic.run();

		var expected = """
			child_limit_static/
			├─ file_0_1
			├─ folder_1/
			│  ├─ file_1_1
			│  ├─ file_1_2
			│  ├─ file_1_3
			│  └─ ...
			├─ folder_2/
			│  ├─ file_2_1
			│  ├─ file_2_2
			│  ├─ file_2_3
			│  └─ ...
			└─ ...""";

		assertThat(result).isEqualTo(expected);
	}

}