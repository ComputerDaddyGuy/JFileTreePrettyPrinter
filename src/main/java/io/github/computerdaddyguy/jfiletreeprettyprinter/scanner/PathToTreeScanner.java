package io.github.computerdaddyguy.jfiletreeprettyprinter.scanner;

import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PathToTreeScanner {

	TreeEntry scan(Path fileOrDir);

	/**
	 * Creates a new path handler with given options.
	 * @return
	 */
	static PathToTreeScanner create(ScanningOptions options) {
		return new DefaultPathToTreeScanner(options);
	}

}
