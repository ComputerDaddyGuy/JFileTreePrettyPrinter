package io.github.computerdaddyguy.jfiletreeprettyprinter.options;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

final class MappingUtils {

	private MappingUtils() {
		// Helper class
	}

	public static Map<String, String> toLowerCaseKeys(Map<String, String> mapping) {
		Objects.requireNonNull(mapping, "mapping is null");
		return mapping.entrySet().stream()
			.collect(
				Collectors.toMap(
					entry -> entry.getKey().toLowerCase(),
					Entry::getValue
				)
			);
	}

}
