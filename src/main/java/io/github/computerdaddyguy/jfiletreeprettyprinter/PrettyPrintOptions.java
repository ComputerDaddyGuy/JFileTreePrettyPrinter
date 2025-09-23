package io.github.computerdaddyguy.jfiletreeprettyprinter;

import io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.RenderingOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.scanner.ScanningOptions;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;
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

	public static final class Sorts {

		private Sorts() {
			// Helper class 
		}

		/**
		 * Comparator that sort paths alphabetically by name.
		 */
		public static final Comparator<Path> BY_NAME = Comparator.comparing((Path path) -> path.getFileName().toString());

		/**
		 * Comparator that sorts paths by file size in ascending order.
		 * Directories are treated as having a size of {@code 0}, so they appear before regular files.
		 */
		public static final Comparator<Path> BY_FILE_SIZE = Comparator.comparing((Path path) -> {
			var file = path.toFile();
			return file.isDirectory() ? 0 : file.length();
		});

		/**
		 * Comparator that places directories before files.
		 * Paths of the same type (both directories or both files) are considered equal.
		 */
		public static final Comparator<Path> DIRECTORY_FIRST = (Path path1, Path path2) -> {
			var isP1Dir = path1.toFile().isDirectory();
			var isP2Dir = path2.toFile().isDirectory();
			if (isP1Dir == isP2Dir) {
				return 0;
			}
			return isP1Dir ? -1 : 1;
		};

		/**
		 * Comparator that places files before directories.
		 * Paths of the same type (both directories or both files) are considered equal.
		 */
		public static final Comparator<Path> DIRECTORY_LAST = DIRECTORY_FIRST.reversed();

		/**
		 * Comparator that sort files by their extension (= the part after the last '.' character in the name).
		 * In case of several extensions (e.g. ".tar.gz"), files are firstly ordered by the "gz", then by "tar.gz":
		 * - ccc.gz
		 * - ddd.gz
		 * - .tar.gz
		 * - aaa.tar.gz
		 * - bbb.tar.gz
		 */

		/**
		 * Comparator that sorts files by their extension(s), defined as the substring(s) after each '.' in the name.
		 * <p>
		 * Examples of ordering:
		 * <ul>
		 *   <li><code>ccc.gz</code></li>
		 *   <li><code>ddd.gz</code></li>
		 *   <li><code>aaa.tar.gz</code></li>
		 *   <li><code>bbb.tar.gz</code></li>
		 *   <li><code>zzz.txt</code></li>
		 * </ul>
		 * <p>
		 * Rules:
		 * <ul>
		 *   <li>If a file has no extension, it is sorted before files with extensions (e.g., <code>README</code> comes before <code>aaa.txt</code>).</li>
		 *   <li>For multi-part extensions (like <code>.tar.gz</code>), comparison is hierarchical:
		 *       first by the last extension (<code>gz</code>), then by the preceding extension (<code>tar.gz</code>), etc.</li>
		 *   <li>If extensions are equal, names are compared alphabetically as a tie-breaker.</li>
		 * </ul>
		 */
		public static final Comparator<Path> BY_EXTENSION = (p1, p2) -> {
			boolean isDir1 = p1.toFile().isDirectory();
			boolean isDir2 = p2.toFile().isDirectory();

			// Directories come first
			if (isDir1) {
				return isDir2 ? 0 : -1;
			}
			if (isDir2) {
				return 1;
			}

			String name1 = p1.getFileName().toString();
			String name2 = p2.getFileName().toString();

			String[] parts1 = name1.split("\\.");
			String[] parts2 = name2.split("\\.");

			// Handle files without extensions
			if (parts1.length == 1 && parts2.length == 1) {
				return name1.compareToIgnoreCase(name2);
			}
			if (parts1.length == 1) {
				return -1; // p1 has no extension --> comes first
			}
			if (parts2.length == 1) {
				return 1;  // p2 has no extension --> comes first
			}

			// Compare extensions starting from the last part
			int i1 = parts1.length - 1;
			int i2 = parts2.length - 1;
			while (i1 >= 1 && i2 >= 1) {
				int cmp = parts1[i1].compareToIgnoreCase(parts2[i2]);
				if (cmp != 0) {
					return cmp;
				}
				i1--;
				i2--;
			}

			// If one has more extension parts, it comes after
			if (i1 > i2)
				return 1;
			if (i2 > i1)
				return -1;

			// Files have same extension (will be sorted by the next comparator - the alphabetical tie-breaker comparator by default)
			return 0;
		};

	}

	private Comparator<Path> pathComparator = Sorts.BY_NAME;

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
		this.pathComparator = Objects.requireNonNull(pathComparator, "pathComparator is null").thenComparing(Sorts.BY_NAME);
		return this;
	}

	// ---------- Filtering ----------

	@Nullable
	private Predicate<Path> pathFilter = null;

	@Override
	@Nullable
	public Predicate<Path> pathFilter() {
		return pathFilter;
	}

	/**
	 * Use a custom filter for retain only some files and/or directories.
	 * 
	 * Filtering is recursive by default: directory's contents will always be traversed.
	 * However, if a directory does not match and none of its children match, the directory itself will not be displayed.
	
	 * @param filter	The filter, <code>null</code> to disable filtering
	 */
	public PrettyPrintOptions filter(Predicate<Path> filter) {
		this.pathFilter = filter;
		return this;
	}

}
