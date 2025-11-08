package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

public record Filter(
	@Valid @Nullable Matcher dir,
	@Valid @Nullable Matcher file
) {

}
