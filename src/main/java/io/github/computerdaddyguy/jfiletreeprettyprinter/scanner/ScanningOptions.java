package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import java.nio.file.Path;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ScanningOptions {

	int getMaxDepth();

	Function<Path, Integer> getChildrenLimitFunction();

}
