package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.recursive.RecursiveFileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.impl.visitor.VisitingFileTreePrettyPrinter;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FileTreePrettyPrinterBuilder {

	private PrettyPrintOptions options = PrettyPrintOptions.createDefault();

	public FileTreePrettyPrinter build() {
		return switch (options.getImplementation()) {
			case VISITOR -> new VisitingFileTreePrettyPrinter(options);
			case RECURSIVE -> new RecursiveFileTreePrettyPrinter(options);
		};
	}

	public FileTreePrettyPrinterBuilder withOptions(PrettyPrintOptions options) {
		this.options = Objects.requireNonNull(options, "options is null");
		return this;
	}

	public FileTreePrettyPrinterBuilder customizeOptions(Function<PrettyPrintOptions, PrettyPrintOptions> optionsCustomizer) {
		Objects.requireNonNull(optionsCustomizer, "optionsCustomizer is null");
		var newOptions = optionsCustomizer.apply(this.options);
		this.options = Objects.requireNonNull(newOptions, "new options after customization is null");
		return this;
	}

}
