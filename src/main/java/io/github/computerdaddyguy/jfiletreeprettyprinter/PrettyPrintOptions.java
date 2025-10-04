package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.RenderingOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.ScanningOptions;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PrettyPrintOptions implements ScanningOptions, RenderingOptions {

	public PrettyPrintOptions() {
		super();
	}

	/**
	 * Create new default options that can be customized using various {@code withXXX} methods.
	 */
	public static PrettyPrintOptions createDefault() {
		return new PrettyPrintOptions();
	}

	// ---------- Child limit function ----------

	private ToIntFunction<Path> childLimit = p -> -1;

	@Override
	public ToIntFunction<Path> getChildLimit() {
		return childLimit;
	}

	/**
	 * Set a fixed limit to the number of visited children, per directory.
	 * Default is no limit.
	 * 
	 * @param childLimit	Limit of visited children per directory, negative value means no limit.
	 */
	public PrettyPrintOptions withChildLimit(int childLimit) {
		return withChildLimit(p -> childLimit);
	}

	/**
	 * Set a function that dynamically limits the number of visited children, depending on the parent directory.
	 * Useful to avoid listing known-ahead huge directories (e.g. node's {@code node_modules}).
	 * Default is no limit.
	 * 
	 * @param childLimitFunction	The dynamic limitation function, cannot be <code>null</code>. A negative computed value means no limit.
	 */
	public PrettyPrintOptions withChildLimit(ToIntFunction<Path> childLimitFunction) {
		this.childLimit = Objects.requireNonNull(childLimitFunction, "childLimitFunction is null");
		return this;
	}

	// ---------- Tree format ----------

	public static enum TreeFormat {

		/**
		 * Uses characters: |--, `-- and │
		 */
		CLASSIC_ASCII,

		/**
		 * Uses characters: ├─, └─ and │
		 */
		UNICODE_BOX_DRAWING,

	}

	private TreeFormat treeFormat = TreeFormat.UNICODE_BOX_DRAWING;

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

	// ---------- Emojis ----------

	private boolean emojis = false;

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

	// ---------- Compact directories ----------

	private boolean compactDirectories = false;

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

	// ---------- Max depth ----------

	private int maxDepth = 20;

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

	// ---------- File sort ----------

	private Comparator<Path> pathComparator = PathSorts.ALPHABETICAL;

	@Override
	public Comparator<Path> pathComparator() {
		return pathComparator;
	}

	/**
	 * Use a custom path comparator to sort files and directories at the same depth level.
	 * 
	 * If the provided comparator considers two paths equal (i.e., returns {@code 0}),
	 * the {@link Sorts#BY_NAME} comparator is applied as a tie-breaker
	 * to ensure consistent ordering across all systems.
	 * 
	 * @param pathComparator The custom comparator
	 */
	public PrettyPrintOptions sort(Comparator<Path> pathComparator) {
		this.pathComparator = Objects.requireNonNull(pathComparator, "pathComparator is null").thenComparing(PathSorts.ALPHABETICAL);
		return this;
	}

	// ---------- Filtering ----------

	private PathMatcher dirMatcher = dir -> true;
	private PathMatcher fileMatcher = dir -> true;

	@Override
	public PathMatcher pathFilter() {
		return PathMatchers.ifMatchesThenElse(PathMatchers.isDirectory(), dirMatcher, fileMatcher);
	}

	/**
	 * Use a custom filter for retain only some directories.
	 * 
	 * Directories that do not pass this filter will not be displayed.
	 * 
	 * @param matcher	The filter to apply on directories, cannot be <code>null</code>
	 */
	public PrettyPrintOptions filterDirectories(@Nullable PathMatcher matcher) {
		this.dirMatcher = Objects.requireNonNull(matcher, "matcher is null");
		return this;
	}

	/**
	 * Use a custom filter for retain only some files.
	 * 
	 * Files that do not pass this filter will not be displayed.
	 * 
	 * @param matcher	The filter to apply on files, cannot be <code>null</code>
	 */
	public PrettyPrintOptions filterFiles(@Nullable PathMatcher matcher) {
		this.fileMatcher = Objects.requireNonNull(matcher, "matcher is null");
		return this;
	}

	// ---------- Line extension ----------

	@Nullable
	private Function<Path, String> lineExtension;

	@Override
	@Nullable
	public Function<Path, String> getLineExtension() {
		return lineExtension;
	}

	/**
	 * Sets a custom line extension function that appends additional text to each
	 * printed line, allowing you to customize the display of files or directories.
	 * <p>
	 * Typical use cases include adding comments, showing file sizes, or displaying metadata.
	 * <p>
	 * The function receives the current {@link Path} displayed on the line
	 * and returns an optional string to be appended.
	 * If the function returns {@code null}, nothing is added.
	 * 
	 * @param lineExtension	the custom line extension function, or {@code null} to disable
	 */
	public PrettyPrintOptions withLineExtension(@Nullable Function<Path, String> lineExtension) {
		this.lineExtension = lineExtension;
		return this;
	}

}
