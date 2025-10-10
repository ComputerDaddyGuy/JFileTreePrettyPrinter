package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.emoji;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
class PathMatcherEmojiFunction implements Function<Path, String> {

	private final List<EmojiMatch> mapping;

	public PathMatcherEmojiFunction(List<EmojiMatch> mapping) {
		super();
		this.mapping = Objects.requireNonNull(mapping, "mapping is null");
	}

	@Override
	public String apply(Path path) {
		return mapping.stream()
			.filter(item -> item.matches(path))
			.findFirst()
			.map(EmojiMatch::emoji)
			.orElse(null);
	}

	public static record EmojiMatch(PathMatcher matcher, String emoji) implements PathMatcher {

		@Override
		public boolean matches(Path path) {
			return matcher.matches(path);
		}

	}

}
