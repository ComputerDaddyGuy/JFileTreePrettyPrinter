package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions.TreeFormat;
import java.nio.file.Path;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface RenderingOptions {

	/**
	 * Are emojis used (filename, etc.)?
	 * @return
	 */
	boolean areEmojisUsed();

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
