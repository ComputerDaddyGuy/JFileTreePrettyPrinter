package io.github.computerdaddyguy.jfiletreeprettyprinter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Nested;
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

	@Nested
	class HasName {

		@Test
		void match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasName("myFile.java").build();
			assertThat(filter.test(path)).isTrue();
		}

		@Test
		void no_match_case_sensitive() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasName("MYFILE.JAVA").build();
			assertThat(filter.test(path)).isFalse();
		}

		@Test
		void no_match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasName("myFile.php").build();
			assertThat(filter.test(path)).isFalse();
		}

	}

	@Nested
	class HasNameIgnoreCase {

		@Test
		void match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasNameIgnoreCase("myFile.java").build();
			assertThat(filter.test(path)).isTrue();
		}

		@Test
		void match_case_sensitive() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasNameIgnoreCase("MYFILE.JAVA").build();
			assertThat(filter.test(path)).isTrue();
		}

		@Test
		void no_match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasNameIgnoreCase("myFile.php").build();
			assertThat(filter.test(path)).isFalse();
		}

	}

	@Nested
	class HasNameMatching {

		@Test
		void match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasNameMatching(Pattern.compile("my.*")).build();
			assertThat(filter.test(path)).isTrue();
		}

		@Test
		void no_match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasNameMatching(Pattern.compile("ma.*")).build();
			assertThat(filter.test(path)).isFalse();
		}

	}

	@Nested
	class HasNameEndingWith {

		@Test
		void match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasNameEndingWith(".java").build();
			assertThat(filter.test(path)).isTrue();
		}

		@Test
		void no_match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasNameEndingWith(".php").build();
			assertThat(filter.test(path)).isFalse();
		}

		@Test
		void no_match_case_sensitive() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasNameEndingWith(".Java").build();
			assertThat(filter.test(path)).isFalse();
		}

	}

	@Nested
	class HasExtension {

		@Test
		void match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasExtension("java").build();
			assertThat(filter.test(path)).isTrue();
		}

		@Test
		void no_match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasExtension("php").build();
			assertThat(filter.test(path)).isFalse();
		}

		@Test
		void no_match_case_sensitive() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().hasExtension("Java").build();
			assertThat(filter.test(path)).isFalse();
		}

		@Test
		void no_match_no_extension() {
			var path = createTempFile("myFilejava");
			var filter = PathPredicates.builder().hasExtension("java").build();
			assertThat(filter.test(path)).isFalse();
		}

	}

	// ---------- Type ----------

	@Nested
	class IsDirectory {

		@Test
		void match() {
			var path = createTempDir("myDir");
			var filter = PathPredicates.builder().isDirectory().build();
			assertThat(filter.test(path)).isTrue();
		}

		@Test
		void no_match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().isDirectory().build();
			assertThat(filter.test(path)).isFalse();
		}

	}

	@Nested
	class IsFile {

		@Test
		void match() {
			var path = createTempFile("myFile.java");
			var filter = PathPredicates.builder().isFile().build();
			assertThat(filter.test(path)).isTrue();
		}

		@Test
		void no_match() {
			var path = createTempDir("myDir");
			var filter = PathPredicates.builder().isFile().build();
			assertThat(filter.test(path)).isFalse();
		}

	}

	// ---------- Hierarchy ----------

	@Nested
	class HasParentMatching {

		@Test
		void null_predicate_throws_NPE() {
			assertThatNullPointerException()
				.isThrownBy(() -> PathPredicates.builder().hasParentMatching(null));
		}

		@Test
		void matches_direct_parent() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));
			var grandChild = Files.createDirectory(child.resolve("grandchild"));

			var filter = PathPredicates.builder().hasParentMatching(
				p -> p.getFileName().toString().equals("child")
			).build();

			assertThat(filter.test(grandChild)).isTrue();
		}

		@Test
		void does_not_match_grandparent() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));
			var grandChild = Files.createDirectory(child.resolve("grandchild"));

			var filter = PathPredicates.builder().hasParentMatching(
				p -> p.getFileName().toString().equals("parent")
			).build();

			assertThat(filter.test(grandChild)).isFalse();
		}

		@Test
		void does_not_match_sibling() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));
			var grandChild1 = Files.createDirectory(child.resolve("grandchild1"));
			var grandChild2 = Files.createDirectory(child.resolve("grandchild2"));

			var filter = PathPredicates.builder().hasParentMatching(
				p -> p.getFileName().toString().equals("grandchild2")
			).build();

			assertThat(filter.test(grandChild1)).isFalse();
		}

		@Test
		void root_has_no_parent() throws IOException {
			var root = Path.of("root");

			var filter = PathPredicates.builder().hasParentMatching(
				p -> true
			).build();

			assertThat(filter.test(root)).isFalse();
		}

	}

	@Nested
	class HasAncestorMatching {

		@Test
		void null_predicate_throws_NPE() {
			assertThatNullPointerException()
				.isThrownBy(() -> PathPredicates.builder().hasAncestorMatching(null));
		}

		@Test
		void matches_direct_parent() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));
			var grandChild = Files.createDirectory(child.resolve("grandchild"));

			var filter = PathPredicates.builder().hasAncestorMatching(
				p -> p.getFileName().toString().equals("child")
			).build();

			assertThat(filter.test(grandChild)).isTrue();
		}

		@Test
		void matches_grandparent() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));
			var grandChild = Files.createDirectory(child.resolve("grandchild"));

			var filter = PathPredicates.builder().hasAncestorMatching(
				p -> p.getFileName().toString().equals("parent")
			).build();

			assertThat(filter.test(grandChild)).isTrue();
		}

		@Test
		void does_not_match_sibling() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));
			var grandChild1 = Files.createDirectory(child.resolve("grandchild1"));
			var grandChild2 = Files.createDirectory(child.resolve("grandchild2"));

			var filter = PathPredicates.builder().hasAncestorMatching(
				p -> p.getNameCount() > 0 && p.getFileName().toString().equals("grandchild2")
			).build();

			assertThat(filter.test(grandChild1)).isFalse();
		}

		@Test
		void root_has_no_parent() throws IOException {
			var root = Path.of("root");

			var filter = PathPredicates.builder().hasAncestorMatching(
				p -> true
			).build();

			assertThat(filter.test(root)).isFalse();
		}

	}

	@Nested
	class HasDirectChildMatching {

		@Test
		void null_predicate_throws_NPE() {
			assertThatNullPointerException()
				.isThrownBy(() -> PathPredicates.builder().hasDirectChildMatching(null));
		}

		@Test
		void matches_direct_child() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));

			var filter = PathPredicates.builder().hasDirectChildMatching(
				p -> p.getFileName().toString().equals("child")
			).build();

			assertThat(filter.test(parent)).isTrue();
		}

		@Test
		void does_not_match_grandchild() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));
			var grandchild = Files.createDirectory(child.resolve("grandchild"));

			var filter = PathPredicates.builder().hasDirectChildMatching(
				p -> p.getFileName().toString().equals("grandchild")
			).build();

			assertThat(filter.test(parent)).isFalse();
		}

		@Test
		void does_not_match_if_not_directory() throws IOException {
			var parent = createTempFile("parent");

			var filter = PathPredicates.builder().hasDirectChildMatching(
				p -> true
			).build();

			assertThat(filter.test(parent)).isFalse();
		}

		@Test
		void does_not_match_if_no_child() throws IOException {
			var parent = createTempDir("parent");

			var filter = PathPredicates.builder().hasDirectChildMatching(
				p -> true
			).build();

			assertThat(filter.test(parent)).isFalse();
		}

	}

	@Nested
	class HasDescendantMatching {

		@Test
		void null_predicate_throws_NPE() {
			assertThatNullPointerException()
				.isThrownBy(() -> PathPredicates.builder().hasDescendantMatching(null));
		}

		@Test
		void matches_grandchild() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));
			var grandchild = Files.createDirectory(child.resolve("grandchild"));

			var filter = PathPredicates.builder().hasDescendantMatching(
				p -> p.getFileName().toString().equals("grandchild")
			).build();

			assertThat(filter.test(parent)).isTrue();
		}

		@Test
		void does_not_match_nonexistent_descendant() {
			var parent = createTempDir("parent");
			parent.resolve("child");

			var filter = PathPredicates.builder().hasDescendantMatching(
				p -> p.getFileName().toString().equals("missing")
			).build();

			assertThat(filter.test(parent)).isFalse();
		}

		@Test
		void does_not_match_if_not_directory() throws IOException {
			var parent = createTempFile("parent");

			var filter = PathPredicates.builder().hasDescendantMatching(
				p -> true
			).build();

			assertThat(filter.test(parent)).isFalse();
		}

		@Test
		void does_not_match_if_no_child() throws IOException {
			var parent = createTempDir("parent");

			var filter = PathPredicates.builder().hasDescendantMatching(
				p -> true
			).build();

			assertThat(filter.test(parent)).isFalse();
		}

	}

	@Nested
	class HasSiblingMatching {

		@Test
		void null_predicate_throws_NPE() {
			assertThatNullPointerException()
				.isThrownBy(() -> PathPredicates.builder().hasSiblingMatching(null));
		}

		@Test
		void matches_sibling() throws IOException {
			var parent = createTempDir("parent");
			var child1 = Files.createDirectory(parent.resolve("child1"));
			var child2 = Files.createDirectory(parent.resolve("child2"));

			var filter = PathPredicates.builder().hasSiblingMatching(
				p -> p.getFileName().toString().equals("child2")
			).build();

			assertThat(filter.test(child1)).isTrue();
		}

		@Test
		void matches_sibling_failed() throws IOException {
			var parent = createTempDir("parent");
			var child1 = Files.createDirectory(parent.resolve("child1"));
			var child2 = Files.createDirectory(parent.resolve("child2"));

			var filter = PathPredicates.builder().hasSiblingMatching(
				p -> p.getFileName().toString().equals("otherChild")
			).build();

			assertThat(filter.test(child1)).isFalse();
		}

		@Test
		void root_has_no_sibling() throws IOException {
			var root = Path.of("root");

			var filter = PathPredicates.builder().hasSiblingMatching(
				p -> true
			).build();

			assertThat(filter.test(root)).isFalse();
		}

		@Test
		void does_not_match_self() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));

			var filter = PathPredicates.builder().hasSiblingMatching(
				p -> true
			).build();

			assertThat(filter.test(child)).isFalse();
		}

		@Test
		void does_not_match_parent_or_child() throws IOException {
			var parent = createTempDir("parent");
			var child = Files.createDirectory(parent.resolve("child"));
			var grandchild = Files.createDirectory(child.resolve("grandchild"));

			var filter = PathPredicates.builder().hasSiblingMatching(
				p -> true
			).build();

			assertThat(filter.test(child)).isFalse();
		}

	}

}
