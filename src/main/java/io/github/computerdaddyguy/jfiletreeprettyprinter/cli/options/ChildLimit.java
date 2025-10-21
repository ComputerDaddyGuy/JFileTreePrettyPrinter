package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
sealed interface ChildLimit {

	/**
	 * 
	 * @param limit
	 */
	@JsonTypeName("static")
	public record StaticLimit(
		@NotNull Integer limit
	) implements ChildLimit {

	}

	/**
	 * 
	 */
	@JsonTypeName("dynamic")
	public record DynamicLimit(
		@Valid @NotNull @NotEmpty List<@Valid @NotNull DynamicLimitItem> limits
	) implements ChildLimit {

	}

	/**
	 *
	 */
	public record DynamicLimitItem(
		@NotNull Integer limit,
		@Valid @NotNull Matcher matcher
	) {

	}

}