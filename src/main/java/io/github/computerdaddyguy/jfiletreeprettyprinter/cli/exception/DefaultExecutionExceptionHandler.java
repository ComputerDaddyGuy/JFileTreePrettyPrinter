package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.exception;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io.ConsoleOutput;
import jakarta.validation.ConstraintViolationException;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

@NullMarked
public class DefaultExecutionExceptionHandler implements IExecutionExceptionHandler {

	private final ConsoleOutput output;

	public DefaultExecutionExceptionHandler(ConsoleOutput output) {
		this.output = Objects.requireNonNull(output, "output is null");
	}

	@Override
	public int handleExecutionException(Exception ex, CommandLine commandLine, ParseResult fullParseResult) throws Exception {
		return switch (ex) {
			case ExternalOptionsException extOptionEx -> handleExternalOptionsException(extOptionEx);
			case ConstraintViolationException violationEx -> handleConstraintViolationException(violationEx);
			default -> handleDefaultException(ex);
		};
	}

	private int handleExternalOptionsException(ExternalOptionsException e) {
		output.printError(e.getMessage() + ": " + e.getOptionsPath().toString());
		e.printStackTrace();
		return 1;
	}

	private int handleConstraintViolationException(ConstraintViolationException e) {
		output.printError(e.getMessage() + ": TODO");
		return 1;
	}

	private int handleDefaultException(Exception e) {
		output.printError("Error while pretty printing: " + e.getMessage());
		return 1;
	}

}
