package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum DepthSymbol {

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
	 * Symbol for representing a "skip", meaning a continue until next file.
	 * Something like "│ ".
	 */
	SKIP,

	/**
	 * Symbol of representing the absence of file.
	 * Contains usually only spaces.
	 */
	NONE;

}