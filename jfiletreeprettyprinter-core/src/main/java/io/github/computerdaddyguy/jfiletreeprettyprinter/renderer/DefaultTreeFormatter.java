package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.TreeFormat;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultTreeFormatter implements TreeFormatter {

	private final TreeFormat treeFormat;
	private final String preIndent;
	private final String postIndent;

	public DefaultTreeFormatter(TreeFormat treeFormat, int spaceBefore, int spaceAfter) {
		this.treeFormat = Objects.requireNonNull(treeFormat, "treeFormat is null");
		this.preIndent = " ".repeat(spaceBefore);
		this.postIndent = " ".repeat(spaceAfter);
	}

	@Override
	public String format(Depth depth) {
		var buff = new StringBuilder();
		var it = depth.getSymbols().iterator();
		while (it.hasNext()) {
			var symbol = it.next();
			if (symbol == TreeSymbol.LAST_FILE && it.hasNext()) {
				symbol = TreeSymbol.EMPTY;
			} else if (symbol == TreeSymbol.NON_LAST_FILE && it.hasNext()) {
				symbol = TreeSymbol.CONTINUATION;
			}
			buff.append(preIndent).append(format(symbol)).append(postIndent);
		}

		return buff.toString();
	}

	private String format(TreeSymbol symbol) {
		return switch (symbol) {
			case NON_LAST_FILE -> treeFormat.getNonLastChildSymbol();
			case LAST_FILE -> treeFormat.getLastChildSymbol();
			case CONTINUATION -> treeFormat.getContinuationSymbol();
			case EMPTY -> treeFormat.getEmptySymbol();
		};
	}

}
