package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.TreeEntryRenderer;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.PathToTreeScanner;
import java.util.Objects;
import java.util.function.UnaryOperator;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FileTreePrettyPrinterBuilder {

	private PrettyPrintOptions options = PrettyPrintOptions.createDefault();

	public FileTreePrettyPrinter build() {
		var scanner = PathToTreeScanner.create(options);
		var renderer = TreeEntryRenderer.create(options);
		return new DefaultFileTreePrettyPrinter(scanner, renderer);
	}

	public FileTreePrettyPrinterBuilder withOptions(PrettyPrintOptions options) {
		this.options = Objects.requireNonNull(options, "options is null");
		return this;
	}

	public FileTreePrettyPrinterBuilder customizeOptions(UnaryOperator<PrettyPrintOptions> optionsCustomizer) {
		Objects.requireNonNull(optionsCustomizer, "optionsCustomizer is null");
		var newOptions = optionsCustomizer.apply(this.options);
		this.options = Objects.requireNonNull(newOptions, "new options after customization is null");
		return this;
	}

}
