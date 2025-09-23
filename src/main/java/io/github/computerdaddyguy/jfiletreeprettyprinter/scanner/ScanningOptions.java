package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ScanningOptions {

	int getMaxDepth();

	ToIntFunction<Path> getChildLimit();

	Comparator<Path> pathComparator();

	@Nullable
	Predicate<Path> pathFilter();

}
