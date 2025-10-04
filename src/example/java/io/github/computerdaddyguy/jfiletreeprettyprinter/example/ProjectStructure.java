package io.github.computerdaddyguy.jfiletreeprettyprinter.example;

import io.github.computerdaddyguy.jfiletreeprettyprinter.ChildLimitBuilder;
import io.github.computerdaddyguy.jfiletreeprettyprinter.FileTreePrettyPrinter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PathMatchers;
import io.github.computerdaddyguy.jfiletreeprettyprinter.PrettyPrintOptions.Sorts;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Function;

public class ProjectStructure {

	public static void main(String[] args) {

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
		var projectFolder = Path.of(".");

		/*
		 * Filter for directories (visit and display only folders that pass this filter)
		 */
		var dirFilter = PathMatchers.noneOf(
			// Exclude these folders from traversal
			PathMatchers.hasRelativePathMatchingGlob(projectFolder, ".git"),
			PathMatchers.hasRelativePathMatchingGlob(projectFolder, ".github"),
			PathMatchers.hasRelativePathMatchingGlob(projectFolder, ".settings"),
			PathMatchers.hasRelativePathMatchingGlob(projectFolder, "src/example"),
			PathMatchers.hasRelativePathMatchingGlob(projectFolder, "src/test"),
			PathMatchers.hasRelativePathMatchingGlob(projectFolder, "target")
		);

		/*
		 * Filter for files (display only files that pass this filter)
		 * Note: files for which the parent folder does not match the directory filter 
		 * are obviously not displayed, even if they pass the file filter.
		 */
		var fileFilter = PathMatchers.allOf(

			// Hide files with names starting with "."
			PathMatchers.not(PathMatchers.hasNameStartingWith(".")),

			// Inside "jfiletreeprettyprinter" folder, keep only "FileTreePrettyPrinter.java"
			// Files in other folders are not restricted by this rule.
			PathMatchers.ifMatchesThenElse(
				/*  if  */ PathMatchers.hasDirectParentMatching(PathMatchers.hasName("jfiletreeprettyprinter")),
				/* then */ PathMatchers.hasName("FileTreePrettyPrinter.java"),
				/* else */ path -> true
			)
		);

		/*
		 * Limit the number of displayed children by directory: some content is not relevant and clutters the final result!
		 */
		var childLimitFunction = ChildLimitBuilder.builder()
			// Hide all files under renderer and scanner packages
			.limit(PathMatchers.hasAbsolutePathMatchingGlob("**/io/github/computerdaddyguy/jfiletreeprettyprinter/renderer"), 0)
			.limit(PathMatchers.hasAbsolutePathMatchingGlob("**/io/github/computerdaddyguy/jfiletreeprettyprinter/scanner"), 0)
			.build();

		/*
		 * Add some comments on a few files and directories
		 */
		Function<Path, String> lineExtension = LineExtensionBuilder.newInstance()
			.add(PathMatchers.hasName("project-structure.png"), "\t// This image")
			.add(PathMatchers.hasName("FileTreePrettyPrinter.java"), "\t// Main entry point")
			.add(PathMatchers.hasName("README.md"), "\t\t// You're reading at this!")
			.addLineBreak(PathMatchers.hasRelativePathMatchingGlob(projectFolder, "src/main/java"))
			.build();

		/*
		 * Sort all paths by directory first (then alphabetically by default)
		 */
		Comparator<Path> pathComparator = Sorts.DIRECTORY_FIRST;

		/*
		 * Build the final FileTreePrettyPrinter
		 */
		var prettyPrinter = FileTreePrettyPrinter.builder()
			.customizeOptions(
				options -> options
					.withEmojis(true) // Use emojis!
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
		var tree = prettyPrinter.prettyPrint(projectFolder);
		System.out.println(tree);

		/*
		 ================================
		        Expected result
		 ================================
		 
			📂 JFileTreePrettyPrinter/
			├─ 📂 assets/
			│  └─ 🖼️ project-structure.png	// This image
			├─ 📂 src/main/java/
			│  └─ 📂 io/github/computerdaddyguy/jfiletreeprettyprinter/
			│     ├─ 📂 renderer/
			│     │  └─ ... (5 files and 2 directories skipped)
			│     ├─ 📂 scanner/
			│     │  └─ ... (4 files skipped)
			│     └─ ☕ FileTreePrettyPrinter.java	// Main entry point
			├─ 🗺️ CHANGELOG.md
			├─ 📖 CONTRIBUTING.md
			├─ 📄 LICENSE
			├─ 📖 README.md		// You're reading at this!
			├─ 🗺️ ROADMAP.md
			├─ 🛡️ SECURITY.md
			├─ 🏗️ pom.xml
			├─ 📖 release_process.md
			└─ 📜 runMutationTests.sh
		 */
	}

}
