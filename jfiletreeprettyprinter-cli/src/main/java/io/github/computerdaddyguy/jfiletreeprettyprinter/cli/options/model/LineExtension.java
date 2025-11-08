package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LineExtension(
	@Valid @NotNull String extension,
	@Valid @NotNull Matcher matcher
) {

}
