package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DepthFormatters {

	private DepthFormatters() {
		// Helper class
	}

	public static DepthFormatter UNICODE_TREE = newDepthFormatter("├─", "└─", "│ ", "  ", 0, 1);

	public static DepthFormatter newDepthFormatter(String nonLastFile, String lastFile, String skip, String none, int spaceBefore, int spaceAfter) {
		return new DefaultDepthFormatter(symbol -> switch (symbol) {
			case NON_LAST_FILE -> nonLastFile;
			case LAST_FILE -> lastFile;
			case SKIP -> skip;
			case NONE -> none;
		}, spaceBefore, spaceAfter);

	}

}
