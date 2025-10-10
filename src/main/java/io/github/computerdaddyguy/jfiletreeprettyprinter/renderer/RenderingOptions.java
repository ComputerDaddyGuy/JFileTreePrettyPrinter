package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.EmojiMapping;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.TreeFormat;
import java.nio.file.Path;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface RenderingOptions {

	/**
	 * The emoji mapping to use
	 * @return
	 */
	EmojiMapping getEmojiMapping();

	/**
	 * Are directories compacted into one entry?
	 * @return
	 */
	boolean areCompactDirectoriesUsed();

	/**
	 * The format used to render file structure tree.
	 * @return
	 */
	TreeFormat getTreeFormat();

	/**
	 * The line extension function.
	 * @return
	 */
	@Nullable
	Function<Path, String> getLineExtension();

}
