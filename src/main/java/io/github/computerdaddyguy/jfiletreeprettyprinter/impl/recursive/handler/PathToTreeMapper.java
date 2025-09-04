package io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.handler;

import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.TreeEntry;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PathToTreeMapper {

	TreeEntry handle(int depth, Path fileOrDir);

	/**
	 * Creates a new path handler with given options.
	 * @return
	 */
	static PathToTreeMapper create(PrettyPrintOptions options) {
		return new DefaultPathToTreeMapper(options);
	}

}
