package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.exception;

import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ExternalOptionsException extends RuntimeException {

	@Nullable
	private final transient Path optionsPath;

	public ExternalOptionsException(@Nullable Path optionsPath, String message, Throwable cause) {
		super(message, cause);
		this.optionsPath = optionsPath;
	}

	public ExternalOptionsException(@Nullable Path optionsPath, String message) {
		super(message);
		this.optionsPath = optionsPath;
	}

	@Nullable
	public Path getOptionsPath() {
		return optionsPath;
	}

}
