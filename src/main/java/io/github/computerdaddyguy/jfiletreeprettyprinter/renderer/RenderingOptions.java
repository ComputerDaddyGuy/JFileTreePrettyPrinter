package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions.TreeFormat;
import org.jspecify.annotations.NullMarked;

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

}
