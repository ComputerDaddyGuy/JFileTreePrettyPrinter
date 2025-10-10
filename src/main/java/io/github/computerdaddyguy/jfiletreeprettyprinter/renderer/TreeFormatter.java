package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.TreeFormat;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface TreeFormatter {

	String format(Depth depth);

	static TreeFormatter getInstance(TreeFormat treeFormat) {
		return new DefaultTreeFormatter(treeFormat, 0, 1);
	}

}
