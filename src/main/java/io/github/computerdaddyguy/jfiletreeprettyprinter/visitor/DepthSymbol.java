package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum DepthSymbol {

	NON_LAST_FILE,

	LAST_FILE,

	SKIP,

	NONE;

}