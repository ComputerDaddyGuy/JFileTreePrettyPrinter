package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

public record ExternalOptions(
	@Nullable Boolean emojis,
	@Valid @Nullable ChildLimit childLimit
) {

}
