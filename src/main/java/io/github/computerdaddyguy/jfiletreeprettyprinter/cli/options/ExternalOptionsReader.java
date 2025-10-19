package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.RecordUtils;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ExternalOptions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import tools.jackson.databind.json.JsonMapper;

public class ExternalOptionsReader {

	public void readOptions() {

		/*
		 * READ
		 */
		var mapper = JsonMapper.builder()
//			.withConfigOverride(
//				Object.class, cfg -> {
//					cfg.setNullHandling(JsonSetter.Value.forValueNulls(Nulls.FAIL, Nulls.FAIL));
//				}
//			)
			.build();
		var path = ".prettyprint";
		ExternalOptions externalOptions = mapper.readValue(Path.of(path), ExternalOptions.class);

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

		/*
		 * PROCESS
		 */
		System.out.println("OK -->\n" + RecordUtils.toTextBlock(externalOptions));
	}

	public static void main(String[] args) {
		LogManager.getLogManager().reset();
		Logger.getLogger("org.hibernate.validator").setLevel(Level.OFF);

		var reader = new ExternalOptionsReader();
		reader.readOptions();
	}

}
