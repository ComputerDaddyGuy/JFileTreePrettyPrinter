package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class PathExtensionEmojiFunction implements Function<Path, String> {

	private final Map<String, String> mapping;

	public PathExtensionEmojiFunction(Map<String, String> mapping) {
		this.mapping = MappingUtils.toLowerCaseKeys(mapping);
	}

	@Override
	@Nullable
	public String apply(Path path) {
		if (path.getFileName() == null) { // if root
			return null;
		}
		var extensions = getExtensions(path);
		String emoji = null;
		for (var ext : extensions) {
			emoji = mapping.get(ext);
			if (emoji != null) {
				return emoji;
			}
		}
		return null;
	}

	/**
	 * Build the list of all extensions of given path, from most specific to less specific.
	 * For "myFile.txt", return list ["txt"]
	 * For "myFile.foo.bar.baz", return list ["foo.bar.baz", "bar.baz", "baz"]
	 * For "myFile", return empty list (= no extension)
	 */
	private List<String> getExtensions(Path path) {
		String[] parts = path.getFileName().toString().toLowerCase().split("\\.");
		if (parts.length == 1) {
			return List.of(); // No extension
		}
		var extensions = new ArrayList<String>();
		for (int i = parts.length - 1; i >= 1; i--) {
			if (extensions.isEmpty()) {
				extensions.add(parts[i]);
			} else {
				extensions.addFirst(parts[i] + "." + extensions.getFirst());
			}
		}
		return extensions;
	}

}
