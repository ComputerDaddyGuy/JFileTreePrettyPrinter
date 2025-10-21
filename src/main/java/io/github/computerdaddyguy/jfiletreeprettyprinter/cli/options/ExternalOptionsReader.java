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

		if (!optionsPath.toFile().exists()) {
			printFailure(required, "Options file not found: %s", optionsPath);
			return null;
		}

		if (!optionsPath.toFile().isFile()) {
			printFailure(required, "Options file is actually not a file: %s", optionsPath);
			return null;
		}

		if (!optionsPath.toFile().canRead()) {
			printFailure(required, "Options file exist but is not readable: %s", optionsPath);
			return null;
		}

		output.printDebug("Options file found: %s", optionsPath);

		ExternalOptions externalOptions;
		try {
			externalOptions = loadAndValidate(optionsPath);
		} catch (ConstraintViolationException e) {
			printFailure(required, "Invalid options file: %s\n%s", optionsPath, e.getMessage());
			return null;
		} catch (RuntimeException e) {
			printFailure(required, "IO error or malformed options file: %s\n%s", optionsPath, e.getMessage());
			return null;
		}

		return mapToOptions(externalOptions);
	}

	private ExternalOptions loadAndValidate(Path optionsPath) {
		/*
		 * READ
		 */
		var mapper = JsonMapper.builder().build();
		ExternalOptions externalOptions = mapper.readValue(optionsPath, ExternalOptions.class);

		/*
		 * VALIDATE
		 */
		var validatorFactory = Validation.byDefaultProvider()
			.configure()
			.messageInterpolator(new ParameterMessageInterpolator())
			.buildValidatorFactory();
		var validator = validatorFactory.getValidator();

		var violations = validator.validate(externalOptions);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		output.printDebug("%s", RecordUtils.toTextBlock(externalOptions));

		return externalOptions;
	}

	private PrettyPrintOptions mapToOptions(ExternalOptions externalOptions) {
		var options = PrettyPrintOptions.createDefault();

		if (externalOptions.emojis()) {
			options = options.withDefaultEmojis();
		}

		return options;
	}

}
