package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import jakarta.validation.Valid;

record ExternalOptions(
	Boolean emojis,
	@Valid ChildLimit childLimit
) {

	public ExternalOptions(Boolean emojis, ChildLimit childLimit) {
		this.emojis = emojis == null ? false : emojis;
		this.childLimit = childLimit;
	}

}
