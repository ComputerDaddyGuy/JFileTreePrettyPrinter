package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import org.jspecify.annotations.NullMarked;

@NullMarked
enum TreeSymbol {

	/**
	 * Symbol for a file that is not the last in its directory (=has a next file).
	 * Something like "├─".
	 */
	NON_LAST_FILE,

	/**
	 * Symbol for a file that is the last in its directory (=has no next file).
	 * Something like "└─".
	 */
	LAST_FILE,

	/**
	 * Symbol for representing a "continuation", meaning a continue until next file.
	 * Something like "│ ".
	 */
	CONTINUATION,

	/**
	 * Symbol of representing the absence of file.
	 * Contains usually only spaces.
	 */
	EMPTY;

}