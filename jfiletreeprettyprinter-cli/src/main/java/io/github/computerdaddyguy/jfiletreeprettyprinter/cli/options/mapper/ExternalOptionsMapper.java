package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.mapper;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ExternalOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ExternalOptionsMapper {

	PrettyPrintOptions mapToOptions(Path targetPath, ExternalOptions externalOptions);

	static ExternalOptionsMapper createDefault() {
		return new DefaultExternalOptionsMapper();
	}

}
