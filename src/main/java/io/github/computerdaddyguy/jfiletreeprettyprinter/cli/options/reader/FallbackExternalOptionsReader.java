package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.reader;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.exception.ExternalOptionsException;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io.ConsoleOutput;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ExternalOptions;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class FallbackExternalOptionsReader implements ExternalOptionsReader {

	private final ConsoleOutput output;
	private final ExternalOptionsReader delegateReader;
	private final Function<Path, Iterable<Path>> fallbackPathsFunction;

	public FallbackExternalOptionsReader(ConsoleOutput output, ExternalOptionsReader delegateReader, Function<Path, Iterable<Path>> fallbackPathsFunction) {
		super();
		this.output = Objects.requireNonNull(output, "output is null");
		this.delegateReader = Objects.requireNonNull(delegateReader, "delegateReader is null");
		this.fallbackPathsFunction = Objects.requireNonNull(fallbackPathsFunction, "fallbackPathsFunction is null");
	}

	@Override
	@Nullable
	public ExternalOptions readExternalOptions(Path targetPath, @Nullable Path optionsPath) {
		if (optionsPath != null) {
			var options = delegateReader.readExternalOptions(targetPath, optionsPath);
			if (options == null) {
				throw new ExternalOptionsException(optionsPath, "Options file does not exist");
			}
			return options;
		}
		output.printDebug("No options file provided, looking for fallback options files...");
		var fallbackOptions = fallbackPathsFunction.apply(targetPath);
		return readFallbackOptions(targetPath, fallbackOptions);
	}

	@Nullable
	private ExternalOptions readFallbackOptions(Path targetPath, Iterable<Path> fallbackOptionsPaths) {
		for (var fallbackOptionsPath : fallbackOptionsPaths) {
			Objects.requireNonNull(fallbackOptionsPath, "one of fallback option paths is null");
			var options = delegateReader.readExternalOptions(targetPath, fallbackOptionsPath);
			if (options != null) {
				return options;
			}
			output.printDebug("Options file does not exist: " + fallbackOptionsPath.toString());
		}
		output.printDebug("No options file provided/found");
		return null;
	}

}
