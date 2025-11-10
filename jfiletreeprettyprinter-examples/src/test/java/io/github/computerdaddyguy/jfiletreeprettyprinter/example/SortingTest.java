package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SortingTest {

	@Test
	void test() {
		var result = Sorting.run();

		var expected = """
			sorting/
			├─ c_dir/
			│  └─ c_file
			├─ d_dir/
			│  ├─ d_b_dir/
			│  │  └─ d_b_file
			│  └─ d_a_file
			├─ a_file
			├─ b_file
			├─ x_file
			└─ y_file""";

		assertThat(result).isEqualTo(expected);
	}

}