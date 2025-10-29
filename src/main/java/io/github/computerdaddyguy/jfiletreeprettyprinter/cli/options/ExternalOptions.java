package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

record ExternalOptions(
	@Nullable Boolean emojis,
	@Valid @Nullable ChildLimit childLimit
) {

	public ExternalOptions(Boolean emojis, ChildLimit childLimit) {
		this.emojis = emojis;
		this.childLimit = childLimit;
	}

}
