package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
class Depth {

	private final List<TreeSymbol> symbols;

	public static Depth createNewEmpty() {
		return new Depth(List.of());
	}

	protected Depth(List<TreeSymbol> symbols) {
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

	public Depth append(TreeSymbol symbol) {
		var newList = new ArrayList<TreeSymbol>(symbols);
		newList.add(symbol);
		return new Depth(newList);
	}

	public Depth pop() {
		if (symbols.isEmpty()) {
			return this;
		}
		var newList = new ArrayList<TreeSymbol>(symbols);
		newList.removeLast();
		return new Depth(newList);
	}

	public List<TreeSymbol> getSymbols() {
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
