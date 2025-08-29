package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.depth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Depth {

	private final List<DepthSymbol> symbols;

	public Depth() {
		this(List.of());
	}

	protected Depth(List<DepthSymbol> symbols) {
		this.symbols = Objects.requireNonNull(symbols, "symbols is null");
	}

	public boolean isEmpty() {
		return symbols.isEmpty();
	}

	public Depth append(DepthSymbol symbol) {
		var newList = new ArrayList<DepthSymbol>(symbols);
		newList.add(symbol);
		return new Depth(newList);
	}

	public Depth pop() {
		if (symbols.isEmpty()) {
			return this;
		}
		var newList = new ArrayList<DepthSymbol>(symbols);
		newList.removeLast();
		return new Depth(newList);
	}

	public List<DepthSymbol> getSymbols() {
		return symbols;
	}

}
