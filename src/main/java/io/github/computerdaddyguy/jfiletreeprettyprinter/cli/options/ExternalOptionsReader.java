package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.ConsoleOutput;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.ChildLimits;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.json.JsonMapper;

@NullMarked
public class ExternalOptionsReader {

	private final ConsoleOutput output;

	public ExternalOptionsReader(ConsoleOutput output) {
		this.output = output;
	}

	@Nullable
	public PrettyPrintOptions readOptions(Path targetPath, Path optionsPath) {
		optionsPath = optionsPath.toAbsolutePath().normalize();
		return tryCandidate(targetPath, optionsPath, true);
	}

	@Nullable
	public PrettyPrintOptions readOptions(Path targetPath, Iterable<Path> potentialOptions) {

		PrettyPrintOptions options = null;

		for (var candidateOptionPath : potentialOptions) {
			candidateOptionPath = candidateOptionPath.toAbsolutePath().normalize();
			options = tryCandidate(targetPath, candidateOptionPath, false);
			if (options != null) {
				break;
			}
		}

		return options;

	}

	private void printFailure(boolean required, String msg, Object... args) {
		if (required) {
			output.printError(msg, args);
		} else {
			output.printDebug(msg, args);
		}
	}

	@Nullable
	private PrettyPrintOptions tryCandidate(Path targetPath, Path optionsPath, boolean required) {

		var optionsFile = optionsPath.toFile();
		if (!optionsFile.exists()) {
			printFailure(required, "Options file not found: %s", optionsPath);
			return null;
		}

		if (!optionsFile.isFile()) {
			printFailure(required, "Options file is actually not a file: %s", optionsPath);
			return null;
		}

		if (!optionsFile.canRead()) {
			printFailure(required, "Options file exist but is not readable: %s", optionsPath);
			return null;
		}

		output.printDebug("Options file found: %s", optionsPath);

		ExternalOptions externalOptions = load(optionsPath, required);
		validate(optionsPath, externalOptions, required);

		return mapToOptions(targetPath, externalOptions);
	}

	private ExternalOptions load(Path optionsPath, boolean required) {
		try {
			var mapper = JsonMapper.builder().build();
			ExternalOptions externalOptions = mapper.readValue(optionsPath, ExternalOptions.class);
			output.printDebug("%s", RecordUtils.toTextBlock(externalOptions));
			return externalOptions;
		} catch (RuntimeException e) {
			printFailure(required, "IO error or malformed options file: %s\n%s", optionsPath, e.getMessage());
			return null;
		}
	}

	private void validate(Path optionsPath, ExternalOptions externalOptions, boolean required) {
		try {
			var validatorFactory = Validation.byDefaultProvider()
				.configure()
				.messageInterpolator(new ParameterMessageInterpolator())
				.buildValidatorFactory();
			var validator = validatorFactory.getValidator();

			var violations = validator.validate(externalOptions);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}
		} catch (ConstraintViolationException e) {
			printFailure(required, "Invalid options file: %s\n%s", optionsPath, e.getMessage());
		}
	}

	private PrettyPrintOptions mapToOptions(Path targetPath, ExternalOptions externalOptions) {
		var options = PrettyPrintOptions.createDefault();
		options = mapEmojis(options, externalOptions);
		options = mapChildLimit(options, externalOptions, targetPath);
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
			case ChildLimit.StaticLimit staticLimit -> options.withChildLimit(staticLimit.limit());
			case ChildLimit.DynamicLimit dynLimit -> {
				var limitBuilder = ChildLimits.builder();
				for (var limit : dynLimit.limits()) {
					limitBuilder.add(mapMatcher(limit.matcher(), targetPath), limit.limit());
				}
				options.withChildLimit(limitBuilder.build());
				yield options;
			}
		};
	}

	private PathMatcher mapMatcher(Matcher matcher, Path targetPath) {
		return switch (matcher) {
			case Matcher.AlwaysTrue alwaysTrue -> (p) -> true;
			case Matcher.AlwaysFalse alwaysFalse -> (p) -> false;
			case Matcher.AllOf allOf -> PathMatchers.allOf(allOf.matchers().stream().map(subMatcher -> mapMatcher(subMatcher, targetPath)).toList());
			case Matcher.AnyOf anyOf -> PathMatchers.anyOf(anyOf.matchers().stream().map(subMatcher -> mapMatcher(subMatcher, targetPath)).toList());
			case Matcher.NoneOf noneOf -> PathMatchers.noneOf(noneOf.matchers().stream().map(subMatcher -> mapMatcher(subMatcher, targetPath)).toList());
			case Matcher.NameGlob nameGlob -> PathMatchers.hasNameMatchingGlob(nameGlob.glob());
			case Matcher.PathGlob pathGlob -> PathMatchers.hasRelativePathMatchingGlob(targetPath, pathGlob.glob());
		};
	}

}
