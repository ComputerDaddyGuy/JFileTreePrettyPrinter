package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.RenderingOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.ScanningOptions;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.ToIntFunction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PrettyPrintOptions implements ScanningOptions, RenderingOptions {

	private ToIntFunction<Path> childrenLimitFunction = p -> -1;

	private TreeFormat treeFormat = TreeFormat.UNICODE_BOX_DRAWING;
	private boolean emojis = false;
	private boolean compactDirectories = false;
	private int maxDepth = 20;

	public PrettyPrintOptions() {
		super();
	}

	/**
	 * Create new default options that can be customized using various {@code withXXX} methods.
	 */
	public static PrettyPrintOptions createDefault() {
		return new PrettyPrintOptions();
	}

	// ----------------------------------------------

	@Override
	public ToIntFunction<Path> getChildrenLimitFunction() {
		return childrenLimitFunction;
	}

	/**
	 * Set a fixed limit to the number of visited children, per directory.
	 * Default is no limit.
	 * 
	 * @param childrenLimit	Limit of visited children per directory, negative value means no limit.
	 */
	public PrettyPrintOptions withChildrenLimit(int childrenLimit) {
		return withChildrenLimitFunction(p -> childrenLimit);
	}

	/**
	 * Set a function that dynamically limits the number of visited children, depending on the parent directory.
	 * Useful to avoid listing known-ahead huge directories (e.g. node's {@code node_modules}).
	 * Default is no limit.
	 * 
	 * @param childrenLimitFunction	The dynamic limitation function, cannot be <code>null</code>. A negative computed value means no limit.
	 */
	public PrettyPrintOptions withChildrenLimitFunction(ToIntFunction<Path> childrenLimitFunction) {
		this.childrenLimitFunction = Objects.requireNonNull(childrenLimitFunction, "childrenLimitFunction is null");
		return this;
	}

	// ----------------------------------------------

	@Override
	public TreeFormat getTreeFormat() {
		return treeFormat;
	}

	/**
	 * Sets the depth rendering format.
	 * Default is {@link TreeFormat#UNICODE_BOX_DRAWING}.
	 * 
	 * @param treeFormat The format to use, cannot be <code>null</code>.
	 */
	public PrettyPrintOptions withTreeFormat(TreeFormat treeFormat) {
		this.treeFormat = Objects.requireNonNull(treeFormat, "treeFormat is null");
		return this;
	}

	// ----------------------------------------------

	@Override
	public boolean areEmojisUsed() {
		return emojis;
	}

	/**
	 * Whether or not use emojis in directory/filename rendering. Not all terminals supports emojis.
	 * Default is {@code false}.
	 * 
	 * @param useEmojis	{@code true} to use emojis, {@code false} otherwise.
	 */
	public PrettyPrintOptions withEmojis(boolean useEmojis) {
		this.emojis = useEmojis;
		return this;
	}

	// ----------------------------------------------

	@Override
	public boolean areCompactDirectoriesUsed() {
		return compactDirectories;
	}

	/**
	 * Whether or not compact directories chain into a single entry in the rendered tree.
	 * Default is {@code false}.
	 * 
	 * @param compact	{@code true} to compact, {@code false} otherwise.
	 */
	public PrettyPrintOptions withCompactDirectories(boolean compact) {
		this.compactDirectories = compact;
		return this;
	}

	// ----------------------------------------------

	@Override
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * Set the max directory depth from the root.
	 * Default is {@code 20}.
	 * 
	 * @param compact	The max depth, negative value for unlimited depth.
	 */
	public PrettyPrintOptions withMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
		return this;
	}

}
