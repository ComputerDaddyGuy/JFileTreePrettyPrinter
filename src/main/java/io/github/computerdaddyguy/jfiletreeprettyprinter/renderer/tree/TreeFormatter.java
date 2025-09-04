package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.tree;

import io.github.computerdaddyguy.jfiletreeprettyprinter.depth.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.RenderingOptions.TreeFormat;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TreeFormatter {

	String format(Depth depth);

	static TreeFormatter getInstance(TreeFormat format) {
		return switch (format) {
			case CLASSIC_ASCII -> DefaultTreeFormatter.CLASSIC_ASCII;
			case UNICODE_BOX_DRAWING -> DefaultTreeFormatter.UNICODE_BOX_DRAWING;
		};
	}

}
