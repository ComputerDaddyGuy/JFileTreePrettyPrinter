package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import java.nio.file.Path;
import java.util.function.Predicate;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface PathToTreeScanner {

	TreeEntry scan(Path fileOrDir, @Nullable Predicate<Path> filter);

	/**
	 * Creates a new path handler with given options.
	 * @return
	 */
	static PathToTreeScanner create(ScanningOptions options) {
		return new DefaultPathToTreeScanner(options);
	}

}
