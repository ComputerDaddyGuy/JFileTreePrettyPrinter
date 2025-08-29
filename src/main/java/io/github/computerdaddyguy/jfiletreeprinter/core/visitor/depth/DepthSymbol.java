package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum DepthSymbol {

	NON_LAST_FILE,

	LAST_FILE,

	SKIP,

	NONE;

}