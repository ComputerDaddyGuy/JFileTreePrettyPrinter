package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.mapper.ExternalOptionsMapper;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.reader.ExternalOptionsReader;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import java.nio.file.Path;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
class DefaultOptionsLoader implements OptionsLoader {

	private final ExternalOptionsReader reader;
	private final ExternalOptionsMapper mapper;

	public DefaultOptionsLoader(ExternalOptionsReader reader, ExternalOptionsMapper mapper) {
		super();
		this.reader = Objects.requireNonNull(reader, "reader is null");
		this.mapper = Objects.requireNonNull(mapper, "mapper is null");
	}

	@Override
	public PrettyPrintOptions loadOptions(Path targetPath, @Nullable Path optionsPath) {
		var externalOptions = reader.readExternalOptions(targetPath, optionsPath);
		if (externalOptions == null) {
			return PrettyPrintOptions.createDefault();
		}
		return mapper.mapToOptions(targetPath, externalOptions);
	}

}
