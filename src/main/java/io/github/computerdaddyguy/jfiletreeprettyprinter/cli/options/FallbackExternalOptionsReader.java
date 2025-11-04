package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.ConsoleOutput;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class FallbackExternalOptionsReader implements ExternalOptionsReader {

	private final ExternalOptionsReader delegateReader;
	private final Function<Path, Iterable<Path>> fallbackPathsFunction;

	public FallbackExternalOptionsReader(ExternalOptionsReader delegateReader, Function<Path, Iterable<Path>> fallbackPathsFunction) {
		super();
		this.delegateReader = Objects.requireNonNull(delegateReader, "delegateReader is null");
		this.fallbackPathsFunction = Objects.requireNonNull(fallbackPathsFunction, "fallbackPathsFunction is null");
	}

	@Override
	@Nullable
	public ExternalOptions readExternalOptions(ConsoleOutput output, Path targetPath, @Nullable Path optionsPath) {
		if (optionsPath != null) {
			var options = delegateReader.readExternalOptions(output, targetPath, optionsPath);
			if (options == null) {
				throw new ExternalOptionsException(optionsPath, "Options file does not exist");
			}
			return options;
		}
		output.printDebug("No options file provided, looking for fallback options files...");
		var fallbackOptions = fallbackPathsFunction.apply(targetPath);
		return readFallbackOptions(output, targetPath, fallbackOptions);
	}

	@Nullable
	private ExternalOptions readFallbackOptions(ConsoleOutput output, Path targetPath, Iterable<Path> fallbackOptionsPaths) {
		for (var fallbackOptionsPath : fallbackOptionsPaths) {
			Objects.requireNonNull(fallbackOptionsPath, "one of fallback option paths is null");
			var options = delegateReader.readExternalOptions(output, targetPath, fallbackOptionsPath);
			if (options != null) {
				return options;
			}
			output.printDebug("Options file does not exist: " + fallbackOptionsPath.toString());
		}
		output.printDebug("No options file provided/found");
		return null;
	}

}
