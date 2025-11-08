package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PrettyPrintOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.TreeEntryRenderer;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.PathToTreeScanner;
import java.util.Objects;
import java.util.function.UnaryOperator;
import org.jspecify.annotations.NullMarked;

/**
 * Builder for creating {@link FileTreePrettyPrinter} instances with customizable {@link PrettyPrintOptions}.
 * <p>
 * By default, the builder uses {@link PrettyPrintOptions#createDefault()}.
 * You can replace the options entirely with {@link #withOptions(PrettyPrintOptions)}
 * or adjust them incrementally with {@link #customizeOptions(UnaryOperator)}.
 * </p>
 *
 * <h2>Example usage:</h2>
 * <pre>{@code
 * var printer = FileTreePrettyPrinter.builder()
 *     .customizeOptions(options -> options
 *         .withMaxDepth(3)
 *         .withCompactDirectories(true)
 *     .build();
 *
 * printer.prettyPrint("src/main/java");
 * }</pre>
 *
 * @see FileTreePrettyPrinter
 * @see PrettyPrintOptions
 */
@NullMarked
public class FileTreePrettyPrinterBuilder {

	private PrettyPrintOptions options;

	/* package */ FileTreePrettyPrinterBuilder() {
		options = PrettyPrintOptions.createDefault();
	}

	/**
	 * Builds the pretty printer using the configured options.
	 * 
	 * @return	the pretty printer
	 */
	public FileTreePrettyPrinter build() {
		var scanner = PathToTreeScanner.create(options);
		var renderer = TreeEntryRenderer.create(options);
		return new DefaultFileTreePrettyPrinter(scanner, renderer);
	}

	/**
	 * Replaces all previously set options with the given options.
	 * 
	 * @param options	options to use
	 * 
	 * @return	this builder for chaining
	 */
	public FileTreePrettyPrinterBuilder withOptions(PrettyPrintOptions options) {
		this.options = Objects.requireNonNull(options, "options is null");
		return this;
	}

	/**
	 * Customizes the options
	 * 
	 * @param optionsCustomizer	option customizer
	 * 
	 * @return	this builder for chaining
	 */
	public FileTreePrettyPrinterBuilder customizeOptions(UnaryOperator<PrettyPrintOptions> optionsCustomizer) {
		Objects.requireNonNull(optionsCustomizer, "optionsCustomizer is null");
		var newOptions = optionsCustomizer.apply(this.options);
		this.options = Objects.requireNonNull(newOptions, "new options after customization is null");
		return this;
	}

}
