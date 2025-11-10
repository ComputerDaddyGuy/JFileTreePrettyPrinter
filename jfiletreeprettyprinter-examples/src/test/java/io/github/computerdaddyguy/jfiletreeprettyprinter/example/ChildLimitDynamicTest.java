package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ChildLimitDynamicTest {

	@Test
	void test() {
		var result = ChildLimitDynamic.run();

		var expected = """
			child_limit_dynamic/
			├─ file_0_1
			├─ folder_1/
			│  ├─ file_1_1
			│  ├─ file_1_2
			│  ├─ file_1_3
			│  ├─ file_1_4
			│  └─ file_1_5
			└─ node_modules/
			   └─ ...""";

		assertThat(result).isEqualTo(expected);
	}

}
