package io.github.computerdaddyguy.jfiletreeprinter.core.predicates;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class FileVisitorPredicates {

	private FileVisitorPredicates() {
		// Helper class
	}

	public static Predicate<Path> hasName(String name) {
		return hasNameIn(List.of(name));
	}

	public static Predicate<Path> hasNameIn(Collection<String> names) {
		return (path) -> names.contains(path.getFileName().toString());
	}

}
