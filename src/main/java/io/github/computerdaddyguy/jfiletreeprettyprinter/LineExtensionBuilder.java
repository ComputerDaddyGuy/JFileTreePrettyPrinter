package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

/**
 * A builder for constructing functions that provide optional line extensions
 * (such as comments or formatting markers) when pretty-printing file trees.
 * <p>
 * A {@code LineExtensionBuilder} allows you to add rules in the form of
 * {@link Function} objects that map a {@link Path} to an extension string.
 * When the resulting function is applied to a path, rules are evaluated
 * in insertion order, and the first non-{@code null} result is used.
 * <ul>
 *   <li>{@code null} means no extension (line is printed normally).</li>
 *   <li>An empty string means "force line break" in compact directory chains.</li>
 *   <li>Any non-empty string is appended after the path (e.g. a comment).</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * var lineExtension = LineExtensionBuilder.newInstance()
 *     .add(PathMatchers.hasName("README.md"), " // Project documentation")
 *     .addLineBreak(PathMatchers.hasRelativePathMatchingGlob(root, "src/main/java"))
 *     .build();
 * }</pre>
 *
 * The returned {@code Function<Path, String>} can then be passed to
 * {@link PrettyPrintOptions#withLineExtension(Function)}.
 *
 * @see PrettyPrintOptions
 */
@NullMarked
public class LineExtensionBuilder {

	private List<Function<Path, String>> extensions;

	/* package */ LineExtensionBuilder() {
		this.extensions = new ArrayList<>();
	}

	/**
	 * Builds the final function mapping a {@link Path} to an extension string.
	 * <p>
	 * The function applies the registered rules in insertion order.
	 * The first rule returning a non-{@code null} value determines the extension.
	 * If none match, the function returns {@code null}.
	 *
	 * @return a function mapping paths to extensions
	 */
	public Function<Path, String> build() {
		var immutExtensions = List.copyOf(extensions);
		return path -> {
			String result = LineExtensions.NO_EXTENSION;
			for (var rule : immutExtensions) {
				result = rule.apply(path);
				if (!Objects.equals(result, LineExtensions.NO_EXTENSION)) {
					break;
				}
			}
			return result;
		};
	}

	/**
	 * Adds a custom line extension rule.
	 * <p>
	 * The function should return either:
	 * <ul>
	 *   <li>{@code null} to indicate "no extension".</li>
	 *   <li>an empty string to force a line break.</li>
	 *   <li>a non-empty string to append after the path.</li>
	 * </ul>
	 *
	 * @param lineExtension a function mapping paths to extensions (non-null)
	 * @return this builder (for chaining)
	 * @throws NullPointerException if {@code lineExtension} is null
	 */
	public LineExtensionBuilder add(Function<Path, String> lineExtension) {
		Objects.requireNonNull(lineExtension, "lineExtension is null");
		this.extensions.add(lineExtension);
		return this;
	}

	/**
	 * Adds a rule that appends the given extension when the matcher matches.
	 * <p>
	 * If the matcher does not match, the rule returns {@code null}.
	 *
	 * @param pathMatcher the matcher to test paths against (non-null)
	 * @param extension   the extension string to return when matched
	 * @return this builder (for chaining)
	 * @throws NullPointerException if {@code pathMatcher} is null
	 */
	public LineExtensionBuilder add(PathMatcher pathMatcher, String extension) {
		Objects.requireNonNull(pathMatcher, "pathMatcher is null");
		return add(path -> pathMatcher.matches(path) ? extension : LineExtensions.NO_EXTENSION);
	}

	/**
	 * Adds a rule that forces a line break (instead of appending text)
	 * whenever the given matcher matches.
	 *
	 * @param pathMatcher the matcher to test paths against (non-null)
	 * @return this builder (for chaining)
	 */
	public LineExtensionBuilder addLineBreak(PathMatcher pathMatcher) {
		return add(pathMatcher, LineExtensions.LINE_BREAK_EXTENSION);
	}

}
