package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ExternalOptionsException extends RuntimeException {

	private final Path optionsPath;

	public ExternalOptionsException(Path optionsPath, String message, Throwable cause) {
		super(message, cause);
		this.optionsPath = optionsPath;
	}

	public ExternalOptionsException(Path optionsPath, String message) {
		super(message);
		this.optionsPath = optionsPath;
	}

	public Path getOptionsPath() {
		return optionsPath;
	}

}
