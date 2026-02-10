package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.ChildLimits;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.LineExtensions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathMatchers;
import io.github.computerdaddyguy.jfiletreeprettyprinter.options.PathSorts;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Function;

public class ProjectStructure {

	/*
	 ================================
	        Expected result
	 ================================
	 
	📂 JFileTreePrettyPrinter/
	├─ 📂 jfiletreeprettyprinter-core/	// The Java lib
	│  ├─ 📂 src/main/java/io/github/computerdaddyguy/jfiletreeprettyprinter/
	│  │  └─ ☕ FileTreePrettyPrinter.java	// Lib main entry point
	│  └─ ...
	├─ 📂 jfiletreeprettyprinter-examples/	// Some examples
	│  └─ ...
	├─ 📂 jfiletreeprettyprinter-cli/	// Everything to build the executable
	│  └─ ...
	├─ 📂 assets/
	│  └─ 🖼️ project-structure.png	// This image
	├─ 📂 docs/
	│  ├─ 📝 How-to-build-a-native-executable-locally.md
	│  ├─ 📝 Release-process.md
	│  └─ 📝 Renew-sonar-token.md
	├─ 🆕 CHANGELOG.md
	├─ 🤝 CONTRIBUTING.md
	├─ ⚖️ LICENSE
	├─ 📘 README.md		// You're reading at this!
	├─ 🗺️ ROADMAP.md
	├─ 🛡️ SECURITY.md
	├─ 🛠️ pom.xml
	└─ 📜 runMutationTests.sh
	
	 */

	public static void main(String[] args) {
		System.out.println(run());
	}

	public static String run() {

		/*
		 * ==========================================================================================
		 * 
		 * Complete example code that pretty prints JFileTreePrettyPrint own project structure.
		 * See the result in image at: https://github.com/ComputerDaddyGuy/JFileTreePrettyPrinter
		 * 
		 * ==========================================================================================
		 */

		/*
		 * The folder to pretty print (= the JFileTreePrettyPrint project root)
		 */
		var projectFolder = Path.of("..");

		/*
		 * Filter for directories (visit and display only folders that pass this filter)
		 */
		var dirFilter = PathMatchers.anyOf(
			PathMatchers.hasAnyDescendantMatching(PathMatchers.hasName("FileTreePrettyPrinter.java")),
			PathMatchers.noneOf(
				// Exclude these folders from traversal
				PathMatchers.hasNameStartingWith("."),
				PathMatchers.hasRelativePathMatchingGlob(projectFolder, "target"),
				PathMatchers.hasRelativePathMatchingGlob(projectFolder, "src"),
				PathMatchers.hasRelativePathMatchingGlob(projectFolder, "*/src/**")
			)
		);

		/*
		 * Filter for files (display only files that pass this filter)
		 * Note: files for which the parent folder does not match the directory filter 
		 * are obviously not displayed, even if they pass the file filter.
		 */
		var fileFilter = PathMatchers.anyOf(
			PathMatchers.hasAbsolutePathMatchingGlob("**/jfiletreeprettyprinter-core/**/FileTreePrettyPrinter.java"),
			PathMatchers.noneOf(
				PathMatchers.hasAbsolutePathMatchingGlob("**/jfiletreeprettyprinter-core/**"),
				PathMatchers.hasNameStartingWith(".")
			)
		);

		PathMatchers.ifMatchesThenElse(
			PathMatchers.hasAbsolutePathMatchingGlob("**/jfiletreeprettyprinter-core/src/main/java/io/github/computerdaddyguy/jfiletreeprettyprinter"),
			PathMatchers.hasName("FileTreePrettyPrinter.java"),
			PathMatchers.noneOf(
				PathMatchers.hasNameStartingWith(".")
			)
		);

		/*
		 * Limit the number of displayed children by directory: some content is not relevant and clutters the final result!
		 */
		var childLimitFunction = ChildLimits.builder()
			// Hide all files under renderer and scanner packages
			.add(PathMatchers.hasAbsolutePathMatchingGlob("**/jfiletreeprettyprinter-cli"), 0)
			.add(PathMatchers.hasAbsolutePathMatchingGlob("**/jfiletreeprettyprinter-examples"), 0)
			.add(PathMatchers.hasAbsolutePathMatchingGlob("**/jfiletreeprettyprinter-core"), 1)
			.build();

		/*
		 * Add some comments on a few files and directories
		 */
		Function<Path, String> lineExtension = LineExtensions.builder()
			.add(PathMatchers.hasName("project-structure.png"), "\t// This image")
			.add(PathMatchers.hasName("README.md"), "\t\t// You're reading at this!")
			.add(PathMatchers.hasName("FileTreePrettyPrinter.java"), "\t// Lib main entry point")
			.add(PathMatchers.hasName("jfiletreeprettyprinter-cli"), "\t// Everything to build the executable")
			.add(PathMatchers.hasName("jfiletreeprettyprinter-core"), "\t// The Java lib")
			.add(PathMatchers.hasName("jfiletreeprettyprinter-examples"), "\t// Some examples")
			.addLineBreak(PathMatchers.hasRelativePathMatchingGlob(projectFolder, "src/main/java"))
			.build();

		/*
		 * Sort all paths setting a precedence value. Default precedence is "0", lower values have higher precedence.
		 * Items having the same precedence value are then sorted alphabetically by default.
		 */
		Comparator<Path> pathComparator = PathSorts.builder()
			// 
			.add(PathMatchers.hasName("jfiletreeprettyprinter-core"), -100)
			.add(PathMatchers.hasName("jfiletreeprettyprinter-examples"), -90)
			.add(PathMatchers.hasName("jfiletreeprettyprinter-cli"), -80)
			.add(PathMatchers.hasName("assets"), -70)
			.add(PathMatchers.hasName("docs"), -60)
			.add(PathMatchers.isDirectory(), -1)
			.build();

		/*
		 * Build the final FileTreePrettyPrinter
		 */
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(
				options -> options
					.withDefaultEmojis() // Use emojis!
					.withCompactDirectories(true) // Inline directory chains: "src/main/java/..."
					.filterDirectories(dirFilter)
					.filterFiles(fileFilter)
					.withChildLimit(childLimitFunction)
					.withLineExtension(lineExtension)
					.sort(pathComparator)
			)
			.build();

		/*
		 * Pretty print and display the result!
		 */
		return prettyPrinter.prettyPrint(projectFolder);
	}

}
