package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.Comparator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PathSortBuilderTest {

	private static void assertCompare(Comparator<Path> comparator, Path pathFirst, Path pathSecond) {
		assertThat(comparator.compare(pathFirst, pathSecond)).isNegative();
		assertThat(comparator.compare(pathSecond, pathFirst)).isPositive();
		assertThat(comparator.compare(pathFirst, pathFirst)).isZero();
		assertThat(comparator.compare(pathSecond, pathSecond)).isZero();
	}

	@Nested
	class AddFunction {

		@Test
		void when_compare_equal_then_alphabetical() {
			var comparator = PathSorts.builder()
				.add(p -> 10)
				.build();

			var pathA = Path.of("A");
			var pathB = Path.of("B");

			assertCompare(comparator, pathA, pathB);
		}

		@Test
		void nominal() {
			var comparator = PathSorts.builder()
				.add(p -> p.getFileName().toString().equals("A") ? 2 : 1)
				.build();

			var pathA = Path.of("A");
			var pathB = Path.of("B");

			assertCompare(comparator, pathB, pathA);
		}

	}

	@Nested
	class AddMatcher {

		@Test
		void when_compare_equal_then_alphabetical() {
			var comparator = PathSorts.builder()
				.add(p -> true, 10)
				.build();

			var pathA = Path.of("A");
			var pathB = Path.of("B");

			assertCompare(comparator, pathA, pathB);
		}

		@Test
		void nominal() {
			var comparator = PathSorts.builder()
				.add(p -> p.getFileName().toString().equals("A"), 2)
				.add(p -> p.getFileName().toString().equals("B"), 1)
				.build();

			var pathA = Path.of("A");
			var pathB = Path.of("B");

			assertCompare(comparator, pathB, pathA);
		}

	}

	@Nested
	class AddFirst {

		@Test
		void when_compare_equal_then_alphabetical() {
			var comparator = PathSorts.builder()
				.addFirst(p -> p.getFileName().toString().equals("A"))
				.addFirst(p -> p.getFileName().toString().equals("B"))
				.build();

			var pathA = Path.of("A");
			var pathB = Path.of("B");

			assertCompare(comparator, pathA, pathB);
		}

	}

	@Nested
	class AddLast {

		@Test
		void when_compare_equal_then_alphabetical() {
			var comparator = PathSorts.builder()
				.addFirst(p -> p.getFileName().toString().equals("A"))
				.addFirst(p -> p.getFileName().toString().equals("B"))
				.build();

			var pathA = Path.of("A");
			var pathB = Path.of("B");

			assertCompare(comparator, pathA, pathB);
		}

	}

}
