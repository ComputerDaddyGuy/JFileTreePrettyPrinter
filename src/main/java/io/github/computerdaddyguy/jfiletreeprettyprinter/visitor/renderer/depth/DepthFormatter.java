package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.depth;

import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.RenderingOptions.DepthFormat;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DepthFormatter {

	String format(Depth depth);

	static DepthFormatter getInstance(DepthFormat format) {
		return switch (format) {
			case CLASSIC_ASCII -> DefaultDepthFormatter.CLASSIC_ASCII;
			case UNICODE_BOX_DRAWING -> DefaultDepthFormatter.UNICODE_BOX_DRAWING;
		};
	}

}
