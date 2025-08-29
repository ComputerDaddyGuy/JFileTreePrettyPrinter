package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RenderingOptions {

	/**
	 * Are emojis used (filename, etc.)?
	 * @return
	 */
	boolean useEmojis();

	/**
	 * The format used to render file structure depth.
	 * @return
	 */
	DepthFormat depthFormat();

	enum DepthFormat {

		/**
		 * Uses characters: |--, ` and │
		 */
		CLASSIC_ASCII,

		/**
		 * Uses characters: ├─, └─ and │
		 */
		UNICODE_BOX_DRAWING,

	}

}
