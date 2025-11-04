package io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import java.nio.file.Path;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ExternalOptionsMapper {

	PrettyPrintOptions mapToOptions(Path targetPath, @Nullable ExternalOptions externalOptions);

	static ExternalOptionsMapper createDefault() {
		return new DefaultExternalOptionsMapper();
	}

}
