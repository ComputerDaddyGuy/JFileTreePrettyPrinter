package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.ConsoleOutput;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import java.nio.file.Path;
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
	public PrettyPrintOptions readOptions(Path optionsPath) {
		optionsPath = optionsPath.toAbsolutePath().normalize();
		return tryCandidate(optionsPath, true);
	}

	@Nullable
	public PrettyPrintOptions readOptions(Iterable<Path> potentialOptions) {

		PrettyPrintOptions options = null;

		for (var candidateOptionPath : potentialOptions) {
			candidateOptionPath = candidateOptionPath.toAbsolutePath().normalize();
			options = tryCandidate(candidateOptionPath, false);
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
	private PrettyPrintOptions tryCandidate(Path optionsPath, boolean required) {

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

		return mapToOptions(externalOptions);
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

	private PrettyPrintOptions mapToOptions(ExternalOptions externalOptions) {
		var options = PrettyPrintOptions.createDefault();

		if (Boolean.TRUE.equals(externalOptions.emojis())) {
			options = options.withDefaultEmojis();
		}

		return options;
	}

}
