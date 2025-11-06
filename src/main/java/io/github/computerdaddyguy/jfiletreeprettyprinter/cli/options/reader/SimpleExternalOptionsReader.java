package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.reader;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.RecordUtils;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.exception.ExternalOptionsException;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io.ConsoleOutput;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ExternalOptions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import java.nio.file.Path;
import java.util.Objects;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.dataformat.yaml.YAMLMapper;

@NullMarked
class SimpleExternalOptionsReader implements ExternalOptionsReader {

	private final ConsoleOutput output;

	public SimpleExternalOptionsReader(ConsoleOutput output) {
		this.output = Objects.requireNonNull(output, "output is null");
	}

	@Override
	@Nullable
	public ExternalOptions readExternalOptions(Path targetPath, @Nullable Path optionsPath) {

		if (optionsPath == null) {
			throw new ExternalOptionsException(optionsPath, "null options path");
		}

		optionsPath = optionsPath.toAbsolutePath().normalize();
		var optionsFile = optionsPath.toFile();
		if (!optionsFile.exists()) {
			return null;
		}
		if (!optionsFile.isFile()) {
			throw new ExternalOptionsException(optionsPath, "Options file is actually not a file");
		}
		if (!optionsFile.canRead()) {
			throw new ExternalOptionsException(optionsPath, "Options file exist but is not readable");
		}

		output.printDebug("Options file found: %s", optionsPath);

		var externalOptions = load(output, optionsPath);
		validate(output, optionsPath, externalOptions);
		return externalOptions;
	}

	private ExternalOptions load(ConsoleOutput output, Path optionsPath) {
		try {
			var mapper = YAMLMapper.builder().build();
			ExternalOptions externalOptions = mapper.readValue(optionsPath, ExternalOptions.class);

			if (output.isDebugEnabled()) { // Trick to avoid RecordUtils.toTextBlock() to evaluate if not needed
				output.printDebug("%s", RecordUtils.toTextBlock(externalOptions));
			}
			return externalOptions;
		} catch (RuntimeException e) {
			throw new ExternalOptionsException(optionsPath, "IO error or malformed options file", e);
		}
	}

	private void validate(ConsoleOutput output, Path optionsPath, ExternalOptions externalOptions) {
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

			output.printDebug("Options file is valid");
		} catch (ConstraintViolationException e) {
			throw new ExternalOptionsException(optionsPath, "Invalid options file", e);
		}
	}

}
