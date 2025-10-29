package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

record ExternalOptions(
	@Nullable Boolean emojis,
	@Valid @Nullable ChildLimit childLimit
) {

}
