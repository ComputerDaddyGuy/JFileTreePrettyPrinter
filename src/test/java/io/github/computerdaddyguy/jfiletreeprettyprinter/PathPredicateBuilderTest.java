package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PathPredicateBuilderTest {

	@TempDir
	private Path root;

	private Path createTempFile(String fileName) {
		var newFile = root.resolve(fileName);
		try {
			var created = newFile.toFile().createNewFile();
			if (!created) {
				throw new IllegalStateException("Unable to create file: " + newFile);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to create file: " + newFile, e);
		}
		return newFile;
	}

	private Path createTempDir(String dirName) {
		var newFile = root.resolve(dirName);
		var created = newFile.toFile().mkdir();
		if (!created) {
			throw new IllegalStateException("Unable to create directory: " + newFile);
		}
		return newFile;
	}

	// ---------- General predicates ----------

	@Test
	void noPredicate_then_nulll() {
		var filter = PathPredicates.builder().build();
		assertThat(filter).isNull();
	}

	@Test
	void pathTest_match() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().pathTest(p -> p.equals(path)).build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void pathTest_noMatch() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().pathTest(p -> p.equals(new Object())).build();
		assertThat(filter.test(path)).isFalse();
	}

	@Test
	void fileTest_match() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().fileTest(p -> p.equals(path.toFile())).build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void fileTest_noMatch() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().fileTest(p -> p.equals(new Object())).build();
		assertThat(filter.test(path)).isFalse();
	}

	// ---------- Name ----------

	@Test
	void hasName_match() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasName("myFile.java").build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void hasName_is_case_sensitive() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasName("myfile.java").build();
		assertThat(filter.test(path)).isFalse();
	}

	@Test
	void hasNameIgnoreCase_match() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasNameIgnoreCase("myFile.java").build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void hasNameIgnoreCase_is_case_insensitive() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasNameIgnoreCase("myfile.java").build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void hasNameMatching_match() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasNameMatching(Pattern.compile("my.*")).build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void hasNameMatching_noMatch() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasNameMatching(Pattern.compile("ma.*")).build();
		assertThat(filter.test(path)).isFalse();
	}

	@Test
	void hasNameEndingWith_match() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasNameEndingWith(".java").build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void hasNameEndingWith_is_case_sensitive() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasNameEndingWith(".Java").build();
		assertThat(filter.test(path)).isFalse();
	}

	@Test
	void hasExtension_match() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasExtension("java").build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void hasExtension_is_case_sensitive() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().hasExtension("Java").build();
		assertThat(filter.test(path)).isFalse();
	}

	// ---------- Type ----------

	@Test
	void isDirectory_match() {
		var path = createTempDir("myDir");
		var filter = PathPredicates.builder().isDirectory().build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void isDirectory_noMatch() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().isDirectory().build();
		assertThat(filter.test(path)).isFalse();
	}

	@Test
	void isFile_match() {
		var path = createTempFile("myFile.java");
		var filter = PathPredicates.builder().isFile().build();
		assertThat(filter.test(path)).isTrue();
	}

	@Test
	void isFile_noMatch() {
		var path = createTempDir("myDir");
		var filter = PathPredicates.builder().isFile().build();
		assertThat(filter.test(path)).isFalse();
	}

}
