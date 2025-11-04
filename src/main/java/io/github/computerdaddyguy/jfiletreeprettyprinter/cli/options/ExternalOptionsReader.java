package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.ConsoleOutput;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ExternalOptionsReader {

	@Nullable
	ExternalOptions readExternalOptions(ConsoleOutput output, Path targetPath, @Nullable Path optionsPath);

	static ExternalOptionsReader createDefault() {
		final String DEFAULT_OPTION_FILENAME = ".prettyprint";

		var simpleReader = new SimpleExternalOptionsReader();
		Function<Path, Iterable<Path>> fallbackOptionsFunction = targetPath -> List.of(
			targetPath.resolve(DEFAULT_OPTION_FILENAME),
			Path.of(".").resolve(DEFAULT_OPTION_FILENAME),
			Path.of(System.getProperty("user.home")).resolve(DEFAULT_OPTION_FILENAME)
		);
		return new FallbackExternalOptionsReader(simpleReader, fallbackOptionsFunction);
	}

}
