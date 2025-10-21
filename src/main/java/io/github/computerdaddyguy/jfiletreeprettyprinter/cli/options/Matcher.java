package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
sealed interface Matcher {

	/**
	 *
	 */
	@JsonTypeName("everything")
	public final record AlwaysTrue() implements Matcher {

	}

	/**
	 *
	 */
	@JsonTypeName("nothing")
	public final record AlwaysFalse() implements Matcher {

	}

	/**
	 *
	 */
	@JsonTypeName("allOf")
	public final record AllOf(
		@Valid @NotNull @NotEmpty List<@Valid @NotNull Matcher> matchers
	) implements Matcher {

	}

	/**
	 *
	 */
	@JsonTypeName("anyOf")
	public final record AnyOf(
		@Valid @NotNull @NotEmpty List<@Valid @NotNull Matcher> matchers
	) implements Matcher {

	}

	/**
	 *
	 */
	@JsonTypeName("noneOf")
	public final record NoneOf(
		@Valid @NotNull @NotEmpty List<@Valid @NotNull Matcher> matchers
	) implements Matcher {

	}

	/**
	 *
	 */
	@JsonTypeName("name")
	public final record NameGlob(
		@NotNull @NotEmpty String glob
	) implements Matcher {

	}

	/**
	 *
	 */
	@JsonTypeName("path")
	public final record PathGlob(
		@NotNull @NotEmpty String glob
	) implements Matcher {

	}

}