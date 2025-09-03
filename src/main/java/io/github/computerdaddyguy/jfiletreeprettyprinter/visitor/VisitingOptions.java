package io.github.computerdaddyguy.jfiletreeprettyprinter.visitor;

import java.nio.file.Path;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface VisitingOptions {

	int getMaxDepth();

	Function<Path, Integer> getChildrenLimitFunction();

}
