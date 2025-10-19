package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model;

import jakarta.validation.Valid;

public record ExternalOptions(
	Boolean emojis,
	@Valid ChildLimit childLimit
) {

	public ExternalOptions(Boolean emojis, ChildLimit childLimit) {
		this.emojis = emojis == null ? false : emojis;
		this.childLimit = childLimit;
	}

}
