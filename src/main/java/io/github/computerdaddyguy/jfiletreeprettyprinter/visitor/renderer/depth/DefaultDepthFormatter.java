package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.renderer.depth;

import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.Depth;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.DepthSymbol;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultDepthFormatter implements DepthFormatter {

	static final DepthFormatter CLASSIC_ASCII = create("|--", "`--", "│  ", "   ", 0, 1);
	static final DepthFormatter UNICODE_BOX_DRAWING = create("├─", "└─", "│ ", "  ", 0, 1);

	private final Function<DepthSymbol, String> printFunction;
	private final String preIndent;
	private final String postIndent;

	public DefaultDepthFormatter(Function<DepthSymbol, String> printFunction, int spaceBefore, int spaceAfter) {
		this.printFunction = Objects.requireNonNull(printFunction, "printFunction is null");
		this.preIndent = " ".repeat(spaceBefore);
		this.postIndent = " ".repeat(spaceAfter);
	}

	static DepthFormatter create(String nonLastFile, String lastFile, String skip, String none, int spaceBefore, int spaceAfter) {
		return new DefaultDepthFormatter(symbol -> switch (symbol) {
			case NON_LAST_FILE -> nonLastFile;
			case LAST_FILE -> lastFile;
			case SKIP -> skip;
			case NONE -> none;
		}, spaceBefore, spaceAfter);

	}

	@Override
	public String format(Depth depth) {
		var buff = new StringBuilder();
		var it = depth.getSymbols().iterator();
		while (it.hasNext()) {
			var symbol = it.next();
			if (symbol == DepthSymbol.LAST_FILE) {
				if (it.hasNext()) {
					symbol = DepthSymbol.NONE;
				}
			} else if (symbol == DepthSymbol.NON_LAST_FILE) {
				if (it.hasNext()) {
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
