package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.jspecify.annotations.Nullable;

public record ExternalOptions(
	@Nullable Boolean emojis,
	@Nullable Boolean compactDirectories,
	@Nullable Integer maxDepth,
	@Valid @Nullable ChildLimit childLimit,
	@Valid @Nullable Filter filter,
	@Valid @Nullable List<@Valid @NotNull LineExtension> lineExtensions
) {

}
