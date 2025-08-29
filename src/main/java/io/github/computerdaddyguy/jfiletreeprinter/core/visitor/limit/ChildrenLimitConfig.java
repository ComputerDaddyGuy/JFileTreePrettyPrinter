package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.limit;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ChildrenLimitConfig {

	private final Function<Path, Integer> childrenLimitFunction;

	private final ChildrenLimitReachedFormatter formatter;

	public ChildrenLimitConfig(
		Function<Path, Integer> childrenLimitFunction,
		ChildrenLimitReachedFormatter formatter
	) {
		super();
		this.childrenLimitFunction = Objects.requireNonNull(childrenLimitFunction, "childrenLimitFunction is null");
		this.formatter = Objects.requireNonNull(formatter, "formatter is null");
	}

	public Function<Path, Integer> getChildrenLimitFunction() {
		return childrenLimitFunction;
	}

	public ChildrenLimitReachedFormatter getFormatter() {
		return formatter;
	}

	public static ChildrenLimitConfig createDefault() {
		return new ChildrenLimitConfig(p -> -1, ChildrenLimitReachedFormatter.createDefault());
	}

	public static ChildrenLimitConfig withLimitFunction(Function<Path, Integer> childrenLimitFunction) {
		return new ChildrenLimitConfig(childrenLimitFunction, ChildrenLimitReachedFormatter.createDefault());
	}

}
