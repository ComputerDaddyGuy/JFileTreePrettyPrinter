package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.emoji;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class SequentialEmojiFunction implements Function<Path, String> {

	private final Iterable<Function<Path, String>> functions;

	public SequentialEmojiFunction(Iterable<Function<Path, String>> functions) {
		super();
		this.functions = Objects.requireNonNull(functions, "functions iterable is null");
	}

	@Override
	@Nullable
	public String apply(Path path) {
		String emoji = null;
		for (var fn : functions) {
			emoji = fn.apply(path);
			if (emoji != null) {
				break;
			}
		}
		return emoji;
	}

}
