package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Comparator;
import java.util.function.ToIntFunction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ScanningOptions {

	int getMaxDepth();

	ToIntFunction<Path> getChildLimit();

	Comparator<Path> pathComparator();

	PathMatcher pathFilter();

}
