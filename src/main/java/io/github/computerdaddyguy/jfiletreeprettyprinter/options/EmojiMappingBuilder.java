package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

import java.nio.file.PathMatcher;
import org.jspecify.annotations.NullMarked;

/**
 * A builder interface for creating an {@link EmojiMapping}, which determines
 * which emoji (if any) should be displayed for each file or directory
 * in the pretty-printed tree.
 *
 * <p>Emoji mappings can be defined based on directory names, file names,
 * file extensions, or arbitrary path matchers.
 * If multiple rules apply to a given path, the following precedence order is used:
 * </p>
 *
 * <ol>
 *   <li>{@code addXXXEmoji(...)} — highest precedence (custom matchers)</li>
 *   <li>{@code setXXXNameEmoji(...)} — name-based mapping</li>
 *   <li>{@code setXXXExtensionEmoji(...)} — extension-based mapping (for files only)</li>
 *   <li>{@code setDefaultXXXEmoji(...)} — lowest precedence (fallback)</li>
 * </ol>
 * 
 * <p>All emoji strings must be non-{@code null}. To override or remove a mapping,
 * create a new builder instance instead of passing {@code null} values.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * var emojiMapping = EmojiMapping.builderFromBlank()
 *     .setDefaultDirectoryEmoji("📂")
 *     .setDefaultFileEmoji("📄")
 *     .setFileExtensionEmoji("java", "☕")
 *     .setFileNameEmoji("Dockerfile", "🐳")
 *     .addFileEmoji(PathMatchers.hasExtension("zip"), "📦")
 *     .build();
 * }</pre>
 *
 * @see EmojiMapping
 * @see EmojiMapping#builderFromBlank()
 * @see EmojiMapping#builderFromDefault()
 * @see PathMatchers
 */
@NullMarked
public interface EmojiMappingBuilder {

	/**
	 * Builds an immutable {@link EmojiMapping} based on the configured rules.
	 *
	 * @return a finalized {@link EmojiMapping} instance
	 */
	EmojiMapping build();

	// ---------- Directories -----------

	/**
	 * Sets the default emoji used for directories when no other directory rule matches.
	 *
	 * @param emoji the emoji string to display for unmatched directories
	 * @return this builder for chaining
	 * @throws NullPointerException if {@code emoji} is {@code null}
	 */
	EmojiMappingBuilder setDefaultDirectoryEmoji(String emoji);

	/**
	 * Associates a specific emoji with a directory of the given name.
	 * The match is case-insensitive and applies only to directories.
	 *
	 * @param dirName the directory name to match (case insensitive)
	 * @param emoji the emoji to display for that directory
	 * @return this builder for chaining
	 * @throws NullPointerException if {@code dirName} or {@code emoji} is {@code null}
	 */
	EmojiMappingBuilder setDirectoryNameEmoji(String dirName, String emoji);

	/**
	 * Adds a custom directory emoji rule using a {@link PathMatcher}.
	 * This provides the highest precedence among directory mappings.
	 *
	 * @param matcher the matcher defining which directories the emoji applies to
	 * @param emoji the emoji to display for matching directories
	 * @return this builder for chaining
	 * @throws NullPointerException if {@code matcher} or {@code emoji} is {@code null}
	 */
	EmojiMappingBuilder addDirectoryEmoji(PathMatcher matcher, String emoji);

	// ---------- Files -----------

	/**
	 * Sets the default emoji used for files when no other file rule matches.
	 *
	 * @param emoji the emoji string to display for unmatched files
	 * @return this builder for chaining
	 * @throws NullPointerException if {@code emoji} is {@code null}
	 */
	EmojiMappingBuilder setDefaultFileEmoji(String emoji);

	/**
	 * Associates a specific emoji with a file of the given name.
	 * The match is case-insensitive and applies only to files.
	 *
	 * @param fileName the exact file name to match
	 * @param emoji the emoji to display for that file
	 * @return this builder for chaining
	 * @throws NullPointerException if {@code fileName} or {@code emoji} is {@code null}
	 */
	EmojiMappingBuilder setFileNameEmoji(String fileName, String emoji);

	/**
	 * Associates an emoji with all files having the specified extension.
	 * The extension should be provided without the leading dot.
	 * Matching is case-insensitive.
	 *
	 * @param fileExtension the file extension (without the leading dot)
	 * @param emoji the emoji to display for files with that extension
	 * @return this builder for chaining
	 * @throws NullPointerException if {@code fileExtension} or {@code emoji} is {@code null}
	 */
	EmojiMappingBuilder setFileExtensionEmoji(String fileExtension, String emoji);

	/**
	 * Adds a custom file emoji rule using a {@link PathMatcher}.
	 * This provides the highest precedence among file mappings.
	 *
	 * @param matcher the matcher defining which files the emoji applies to
	 * @param emoji the emoji to display for matching files
	 * @return this builder for chaining
	 * @throws NullPointerException if {@code matcher} or {@code emoji} is {@code null}
	 */
	EmojiMappingBuilder addFileEmoji(PathMatcher matcher, String emoji);

}
