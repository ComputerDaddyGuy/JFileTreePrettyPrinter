package io.github.computerdaddyguy.jfiletreeprettyprinter;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

public final class PathSorts {

	private PathSorts() {
		// Helper class
	}

	/**
	 * Default alphabetical comparator based on the file name.
	 */
	public static final Comparator<Path> ALPHABETICAL = Comparator.comparing(
		(Path path) -> Optional.ofNullable(path.getFileName())
			.map(Path::toString)
			.orElse("")
	);

	/**
	 * Comparator that sorts paths by file size in ascending order.
	 * Directories are treated as having a size of {@code 0}, so they appear before regular files.
	 */
	public static final Comparator<Path> BY_FILE_SIZE = Comparator.comparing((Path path) -> {
		return PathMatchers.isDirectory().matches(path) ? 0 : path.toFile().length();
	});

	/**
	 * Comparator that places directories before files.
	 * Paths of the same type (both directories or both files) are considered equal.
	 */
	public static final Comparator<Path> DIRECTORY_FIRST = (Path path1, Path path2) -> {
		var isP1Dir = PathMatchers.isDirectory().matches(path1);
		var isP2Dir = PathMatchers.isDirectory().matches(path2);
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
		var isDir1 = PathMatchers.isDirectory().matches(p1);
		var isDir2 = PathMatchers.isDirectory().matches(p2);

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

	/**
	 * Returns a new {@link PathSortBuilder}.
	 *
	 * @return a fresh builder instance
	 */
	public static PathSortBuilder builder() {
		return new PathSortBuilder();
	}

}
