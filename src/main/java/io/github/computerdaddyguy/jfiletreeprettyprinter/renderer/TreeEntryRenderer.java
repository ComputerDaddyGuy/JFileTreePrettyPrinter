package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer;

import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.TreeEntry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TreeEntryRenderer {

	String renderTree(TreeEntry entry);

	/**
	 * Create a new tree renderer, using given options
	 * @return
	 */
	static TreeEntryRenderer create(RenderingOptions options) {
		return new DefaultTreeEntryRenderer(options);
	}

}
