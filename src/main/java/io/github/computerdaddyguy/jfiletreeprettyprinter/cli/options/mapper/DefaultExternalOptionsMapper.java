package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.mapper;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ChildLimit;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ExternalOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.Matcher;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.ChildLimits;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.LineExtensions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

class DefaultExternalOptionsMapper implements ExternalOptionsMapper {

	@Override
	public PrettyPrintOptions mapToOptions(Path targetPath, ExternalOptions externalOptions) {
		var options = PrettyPrintOptions.createDefault();
		options = mapEmojis(options, externalOptions);
		options = mapCompactDirectories(options, externalOptions);
		options = mapMaxDepth(options, externalOptions);
		options = mapChildLimit(options, externalOptions, targetPath);
		options = mapFilter(options, externalOptions, targetPath);
		options = mapLineExtensions(options, externalOptions, targetPath);
		return options;
	}

	private PrettyPrintOptions mapEmojis(PrettyPrintOptions options, ExternalOptions externalOptions) {
		if (Boolean.TRUE.equals(externalOptions.emojis())) {
			return options.withDefaultEmojis();
		}
		return options;
	}

	private PrettyPrintOptions mapCompactDirectories(PrettyPrintOptions options, ExternalOptions externalOptions) {
		if (Boolean.TRUE.equals(externalOptions.compactDirectories())) {
			return options.withCompactDirectories(true);
		}
		return options;
	}

	private PrettyPrintOptions mapMaxDepth(PrettyPrintOptions options, ExternalOptions externalOptions) {
		if (externalOptions.maxDepth() != null) {
			return options.withMaxDepth(externalOptions.maxDepth());
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

	private PrettyPrintOptions mapFilter(PrettyPrintOptions options, ExternalOptions externalOptions, Path targetPath) {
		if (externalOptions.filter() == null) {
			return options;
		}
		if (externalOptions.filter().dir() != null) {
			options = options.filterDirectories(mapMatcher(externalOptions.filter().dir(), targetPath));
		}
		if (externalOptions.filter().file() != null) {
			options = options.filterFiles(mapMatcher(externalOptions.filter().file(), targetPath));
		}
		return options;
	}

	private PrettyPrintOptions mapLineExtensions(PrettyPrintOptions options, ExternalOptions externalOptions, Path targetPath) {
		if (externalOptions.lineExtensions() == null) {
			return options;
		}
		var builder = LineExtensions.builder();
		for (var extension : externalOptions.lineExtensions()) {
			builder.add(mapMatcher(extension.matcher(), targetPath), extension.extension());
		}
		return options.withLineExtension(builder.build());
	}

	private PathMatcher mapMatcher(Matcher matcher, Path targetPath) {
		return switch (matcher) {
			case Matcher.AlwaysTrue() -> p -> true;
			case Matcher.AlwaysFalse() -> p -> false;
			case Matcher.AllOf(var matchers) -> PathMatchers.allOf(matchers.stream().map(subMatcher -> mapMatcher(subMatcher, targetPath)).toList());
			case Matcher.AnyOf(var matchers) -> PathMatchers.anyOf(matchers.stream().map(subMatcher -> mapMatcher(subMatcher, targetPath)).toList());
			case Matcher.NoneOf(var matchers) -> PathMatchers.noneOf(matchers.stream().map(subMatcher -> mapMatcher(subMatcher, targetPath)).toList());
			case Matcher.NameGlob(var glob) -> PathMatchers.hasNameMatchingGlob(glob);
			case Matcher.PathGlob(var glob) -> PathMatchers.hasRelativePathMatchingGlob(targetPath, glob);
		};
	}

}
