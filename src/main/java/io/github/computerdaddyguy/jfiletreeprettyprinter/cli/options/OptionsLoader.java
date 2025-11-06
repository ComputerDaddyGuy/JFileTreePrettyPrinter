package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.io.ConsoleOutput;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.mapper.ExternalOptionsMapper;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.reader.ExternalOptionsReader;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface OptionsLoader {

	PrettyPrintOptions loadOptions(Path targetPath, @Nullable Path optionsPath);

	public static OptionsLoader createDefault(ConsoleOutput output) {
		var reader = ExternalOptionsReader.createDefault(output);
		var mapper = ExternalOptionsMapper.createDefault();
		return new DefaultOptionsLoader(reader, mapper);
	}

}
