package io.github.computerdaddyguy.jfiletreeprinter.core.visitor.limit;

import java.nio.file.Path;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ChildrenLimitReachedFormatter {

	@Nullable
	String formatLimitReached(Set<Path> notVisited);

	static ChildrenLimitReachedFormatter createDefault() {
		return new DefaultChildrenLimitReachedFormatter();
	}

}
