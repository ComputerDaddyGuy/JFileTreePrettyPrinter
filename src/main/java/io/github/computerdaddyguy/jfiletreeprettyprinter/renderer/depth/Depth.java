package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.depth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Depth {

	private final List<DepthSymbol> symbols;

	public static Depth createNewEmpty() {
		return new Depth(List.of());
	}

	protected Depth(List<DepthSymbol> symbols) {
		this.symbols = Objects.requireNonNull(symbols, "symbols is null");
	}

	public boolean isEmpty() {
		return symbols.isEmpty();
	}

	public int getSize() {
		return symbols.size();
	}

	public boolean isRoot() {
		return getSize() == 0;
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

	@Override
	public String toString() {
		return symbols.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbols);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Depth other) {
			return Objects.equals(symbols, other.symbols);
		}
		return false;
	}

}
