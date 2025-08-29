package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DepthFormatter {

	default String format(Depth depth) {
		return format(depth, true);
	}

	String format(Depth depth, boolean hasTarget);

}
