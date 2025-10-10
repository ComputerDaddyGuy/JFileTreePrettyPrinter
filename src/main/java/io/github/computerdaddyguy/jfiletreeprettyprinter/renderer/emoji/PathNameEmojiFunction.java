package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.emoji;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class PathNameEmojiFunction implements Function<Path, String> {

	private final Map<String, String> mapping;

	public PathNameEmojiFunction(Map<String, String> mapping) {
		this.mapping = toLowerCaseKeys(mapping);
	}

	private static Map<String, String> toLowerCaseKeys(Map<String, String> mapping) {
		Objects.requireNonNull(mapping, "mapping is null");
		return mapping.entrySet().stream()
			.collect(
				Collectors.toMap(
					entry -> entry.getKey().toLowerCase(),
					entry -> entry.getValue()
				)
			);
	}

	@Override
	@Nullable
	public String apply(Path path) {
		if (path.getFileName() == null) { // if root
			return null;
		}
		return mapping.get(path.getFileName().toString().toLowerCase());
	}

}
