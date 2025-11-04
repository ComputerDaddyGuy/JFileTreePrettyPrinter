package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.ChildLimits;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import org.jspecify.annotations.Nullable;

class DefaultExternalOptionsMapper implements ExternalOptionsMapper {

	@Override
	public PrettyPrintOptions mapToOptions(Path targetPath, @Nullable ExternalOptions externalOptions) {
		var options = PrettyPrintOptions.createDefault();
		if (externalOptions != null) {
			options = mapEmojis(options, externalOptions);
			options = mapChildLimit(options, externalOptions, targetPath);
		}
		return options;
	}

	private PrettyPrintOptions mapEmojis(PrettyPrintOptions options, ExternalOptions externalOptions) {
		if (Boolean.TRUE.equals(externalOptions.emojis())) {
			return options.withDefaultEmojis();
		}
		return options;
	}

	private PrettyPrintOptions mapChildLimit(PrettyPrintOptions options, ExternalOptions externalOptions, Path targetPath) {
		if (externalOptions.childLimit() == null) {
			return options;
		}
		return switch (externalOptions.childLimit()) {
			case ChildLimit.StaticLimit(var limit) -> options.withChildLimit(limit);
			case ChildLimit.DynamicLimit(var limits) -> {
				var limitBuilder = ChildLimits.builder();
				for (var limit : limits) {
					limitBuilder.add(mapMatcher(limit.matcher(), targetPath), limit.limit());
				}
				options.withChildLimit(limitBuilder.build());
				yield options;
			}
		};
	}

	private PathMatcher mapMatcher(Matcher matcher, Path targetPath) {
		return switch (matcher) {
			case Matcher.AlwaysTrue() -> (p) -> true;
			case Matcher.AlwaysFalse() -> (p) -> false;
			case Matcher.AllOf(var matchers) -> PathMatchers.allOf(matchers.stream().map(subMatcher -> mapMatcher(subMatcher, targetPath)).toList());
			case Matcher.AnyOf(var matchers) -> PathMatchers.anyOf(matchers.stream().map(subMatcher -> mapMatcher(subMatcher, targetPath)).toList());
			case Matcher.NoneOf(var matchers) -> PathMatchers.noneOf(matchers.stream().map(subMatcher -> mapMatcher(subMatcher, targetPath)).toList());
			case Matcher.NameGlob(var glob) -> PathMatchers.hasNameMatchingGlob(glob);
			case Matcher.PathGlob(var glob) -> PathMatchers.hasRelativePathMatchingGlob(targetPath, glob);
		};
	}

}
