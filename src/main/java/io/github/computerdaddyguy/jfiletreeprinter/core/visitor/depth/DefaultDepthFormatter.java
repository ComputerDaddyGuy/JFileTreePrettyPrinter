package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth;

import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultDepthFormatter implements DepthFormatter {

	private final Function<DepthSymbol, String> printFunction;
	private final String preIndent;
	private final String postIndent;

	public DefaultDepthFormatter(Function<DepthSymbol, String> printFunction, int spaceBefore, int spaceAfter) {
		this.printFunction = Objects.requireNonNull(printFunction, "printFunction is null");
		this.preIndent = " ".repeat(spaceBefore);
		this.postIndent = " ".repeat(spaceAfter);
	}

	@Override
	public String format(Depth depth, boolean hasTarget) {
		var buff = new StringBuilder();
		var it = depth.getSymbols().iterator();
		while (it.hasNext()) {
			var symbol = it.next();
			if (symbol == DepthSymbol.LAST_FILE) {
				if (it.hasNext()) {
					symbol = DepthSymbol.NONE;
				} else if (!hasTarget) {
					symbol = DepthSymbol.NONE;
				}
			} else if (symbol == DepthSymbol.NON_LAST_FILE) {
				if (it.hasNext()) {
					symbol = DepthSymbol.SKIP;
				} else if (!hasTarget) {
					symbol = DepthSymbol.SKIP;
				}
			}
			buff.append(format(symbol));
		}

		return buff.toString();
	}

	private String format(DepthSymbol symbol) {
		return preIndent + printFunction.apply(symbol) + postIndent;
	}

}
