package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class PathNameEmojiFunction implements Function<Path, String> {

	private final Map<String, String> mapping;

	public PathNameEmojiFunction(Map<String, String> mapping) {
		this.mapping = MappingUtils.toLowerCaseKeys(mapping);
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
