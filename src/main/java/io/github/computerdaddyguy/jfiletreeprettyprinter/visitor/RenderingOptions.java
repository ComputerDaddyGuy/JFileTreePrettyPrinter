package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor;

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

	enum TreeFormat {

		/**
		 * Uses characters: |--, `-- and │
		 */
		CLASSIC_ASCII,

		/**
		 * Uses characters: ├─, └─ and │
		 */
		UNICODE_BOX_DRAWING,

	}

}
