package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.RenderingOptions.TreeFormat;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DepthFormatter {

	String format(Depth depth);

	static DepthFormatter getInstance(TreeFormat format) {
		return switch (format) {
			case CLASSIC_ASCII -> DefaultDepthFormatter.CLASSIC_ASCII;
			case UNICODE_BOX_DRAWING -> DefaultDepthFormatter.UNICODE_BOX_DRAWING;
		};
	}

}
