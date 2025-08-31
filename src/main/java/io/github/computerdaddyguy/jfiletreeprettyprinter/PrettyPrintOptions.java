package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.RenderingOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.visitor.VisitingOptions;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PrettyPrintOptions implements VisitingOptions, RenderingOptions {

	private Function<Path, Integer> childrenLimitFunction = p -> -1;

	private TreeFormat treeFormat = TreeFormat.UNICODE_BOX_DRAWING;
	private boolean useEmojis = false;

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
	public Function<Path, Integer> getChildrenLimitFunction() {
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
	public PrettyPrintOptions withChildrenLimitFunction(Function<Path, Integer> childrenLimitFunction) {
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
		return useEmojis;
	}

	/**
	 * Whether or not use emojis in directory/filename rendering. Not all terminals supports emojis.
	 * Default is {@code false}.
	 * 
	 * @param useEmojis	{@code true} to use emojis, {@code false} otherwise.
	 */
	public PrettyPrintOptions withEmojis(boolean useEmojis) {
		this.useEmojis = useEmojis;
		return this;
	}

}
