# JFileTreePrettyPrint
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=alert_status&token=42442b67d269c6a17b4578ba2d87731c92b8922a)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=security_rating&token=42442b67d269c6a17b4578ba2d87731c92b8922a)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=vulnerabilities&token=42442b67d269c6a17b4578ba2d87731c92b8922a)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=coverage&token=42442b67d269c6a17b4578ba2d87731c92b8922a)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.computerdaddyguy/jfiletreeprettyprinter.svg?color=blue)](https://maven-badges.herokuapp.com/maven-central/io.github.computerdaddyguy/jfiletreeprettyprinter/)
[![Javadoc](https://javadoc.io/badge2/io.github.computerdaddyguy/jfiletreeprettyprinter/javadoc.svg?color=blue)](https://javadoc.io/doc/io.github.computerdaddyguy/jfiletreeprettyprinter)
[![Apache License 2.0](https://img.shields.io/:license-Apache%20License%202.0-blue.svg)](https://github.com/computerdaddyguy/jfiletreeprettyprinter/blob/main/LICENSE)

**A lightweight Java library for printing directory structures in a clean, tree-like format.** 

Supports various [options](#options) to customize the directories scanning and rendering:
- Filtering & sorting files and folders
- Emojis as file icons 🎉
- Limit displayed children of a folder (fixed value or dynamically)
- Custom line extension (comment, file details, etc.)
- Compact directory chains
- Maximum scanning depth
- Various styles for tree rendering

> [!NOTE]
> JFileTreePrettyPrinter is perfect to explain your project structure!  
> See <a href="https://github.com/ComputerDaddyGuy/JFileTreePrettyPrinter/blob/develop/src/example/java/io/github/computerdaddyguy/jfiletreeprettyprinter/example/ProjectStructure.java">ProjectStructure.java</a> to read the code that generated the tree from the below picture.

<p align="center">
	<img src="https://raw.githubusercontent.com/ComputerDaddyGuy/JFileTreePrettyPrinter/refs/heads/develop/assets/project-structure.png" alt="JFileTreePrettyPrint project structure, using JFileTreePrettyPrint"/>
</p>

> [!IMPORTANT]
> Complete documentation available in [wiki](https://github.com/ComputerDaddyGuy/JFileTreePrettyPrinter/wiki).

* [Import dependency](#import-dependency)
* [Basic usage](#basic-usage)  
* [Options](#options)
* [Changelog](#changelog) 
* [Roadmap](#roadmap) 
* [License](#license) 
* [Contributing & Contact](#contributing--contact) 

# Import dependency
For Maven, import this dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>io.github.computerdaddyguy</groupId>
  <artifactId>jfiletreeprettyprinter</artifactId>
  <version>0.0.5</version>
</dependency>
```

For Gradle:
```
implementation "io.github.computerdaddyguy:jfiletreeprettyprinter:0.0.5"
```

# Basic Usage
```java
// Example: BasicUsage.java
var printer = FileTreePrettyPrinter.createDefault();
var tree = printer.prettyPrint("src/example/resources/base");
System.out.println(tree);
```

Result:
```
base/
├─ businessPlan.pdf
├─ businessProject.pdf
├─ cars/
│  ├─ Ferrari.doc
│  └─ Porsche.doc
├─ diyIdeas.docx
├─ giftIdeas.txt
└─ images/
   ├─ funnyCat.gif
   ├─ holidays/
   │  ├─ meAtTheBeach.jpeg
   │  └─ meAtTheZoo.jpeg
   └─ landscape.jpeg
```

> [!NOTE]
> In case of error while reading directories or files, an [UncheckedIOException](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/UncheckedIOException.html) is thrown. 

# Options

* [Filtering](#filtering)
* [Sorting](#sorting)
* [Emojis ❤️](#emojis-%EF%B8%8F)
* [Child limit](#child-limit)
* [Line extension](#line-extension)
* [Compact directories](#compact-directories)
* [Max depth](#max-depth)
* [Tree format](#tree-format)
  
## Filtering
Files and directories can be selectively included or excluded using a custom `PathMatcher`.

Filtering is independant for files & directories. Files are filtered only if their parent directory pass the directory filter.
If none of some directory's children match, the directory is still displayed.

The `PathMatchers` class provides several ready-to-use methods for creating and combining common matchers.

```java
// Example: Filtering.java
var excludeDirWithNoJavaFiles = PathMatchers.not(PathMatchers.hasNameEndingWith("no_java_file"));
var hasJavaExtension = PathMatchers.hasExtension("java");

var prettyPrinter = FileTreePrettyPrinter.builder()
	.customizeOptions(
		options -> options
			.filterDirectories(excludeDirWithNoJavaFiles)
			.filterFiles(hasJavaExtension)
	)
	.build();
```
```
filtering/
├─ dir_with_java_files/
│  ├─ file_B.java
│  └─ file_E.java
├─ dir_with_nested_java_files/
│  └─ nested_dir_with_java_files/
│     ├─ file_G.java
│     └─ file_J.java
└─ file_A.java
```

## Sorting
Files and directories can be sorted using a custom comparator (default is alphabetical order).
If the provided comparator considers two paths equal (i.e., returns `0`), an alphabetical comparator is applied as a tie-breaker to ensure consistent results across all systems.  

The `PathSorts` class provides a set of basic, ready-to-use comparators, as well as a builder for creating your own tailor-made sort.

```java
// Example: Sorting.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.sort(PathSorts.DIRECTORY_FIRST))
    .build();
```
```
sorting/
├─ c_dir/
│  └─ c_file
├─ d_dir/
│  ├─ d_b_dir/
│  │  └─ d_b_file
│  └─ d_a_file
├─ a_file
├─ b_file
├─ x_file
└─ y_file
```

## Emojis ❤️
If your terminal supports them, you can choose to use emojis.
Folders will have the "📂" emoji, and files will have an emoji depending on their extension (when applicable).

```java
// Example: Emojis.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withEmojis(true))
    .build();
```

```
// Run Emojis.java example for exhaustive list
📂 emojis/
├─ 📦 file.zip
├─ 🐳 Dockerfile
├─ 🤵 Jenkinsfile
├─ ☕ file.java
├─ 📖 readme
├─ ⚙️ file.ini
├─ 📊 file.xlsx
├─ 📃 file.docx
├─ 📕 file.pdf
├─ 🎵 file.mp3
├─ 🖼️ file.jpeg
└─ 🎬 file.avi
```

> [!TIP]
> *Idea for a future version: option to allow custom emoji mapping*

## Child limit
You can set a fixed limit to the number of children displayed for each directory. Each directory and file that pass filter (if set) counts for one.

```java
// Example: ChildLimitStatic.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withChildLimit(3))
    .build();
```

```
child_limit_static/
├─ file_0_1
├─ folder_1/
│  ├─ file_1_1
│  ├─ file_1_2
│  ├─ file_1_3
│  └─ ...
├─ folder_2/
│  ├─ file_2_1
│  ├─ file_2_2
│  ├─ file_2_3
│  └─ ...
└─ ...

```

Or you can also set a limitation function, to dynamically choose the number of children displayed in each directory.
It avoids cluttering the whole console with known large folders (e.g. `node_modules`) but continue to pretty print normally other folders.

Use the `ChildLimits` class to help you build the limit function that fits your needs.

```java
// Example: ChildLimitDynamic.java
var childLimit = ChildLimits.builder()
	.setDefault(ChildLimits.UNLIMITED)            // Unlimited children by default
	.add(PathMatchers.hasName("node_modules"), 0) // Do NOT print any children in "node_modules" folder
	.build();
var prettyPrinter = FileTreePrettyPrinter.builder()
	.customizeOptions(options -> options.withChildLimit(childLimit))
	.build();
```
```
child_limit_dynamic/
├─ file_0_1
├─ folder_1/
│  ├─ file_1_1
│  ├─ file_1_2
│  ├─ file_1_3
│  ├─ file_1_4
│  └─ file_1_5
└─ node_modules/
   └─ ...
```

## Line extension
You can extend each displayed path with additional information by providing a custom `Function<Path, String>`.
This is useful to annotate your tree with comments, display file sizes, or add domain-specific notes.

The function receives the current path and returns an optional string to append (empty string is authorized).
If the function returns `null`, nothing is added.

Use the `LineExtensions` class to help you build line extension functions.

```java
// Example: LineExtension.java
var printedPath = Path.of("src/example/resources/line_extension");

Function<Path, String> lineExtension = LineExtensions.builder()
	.add(PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/api"), "\t\t\t// All API code: controllers, etc.")
	.add(PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/domain"), "\t\t\t// All domain code: value objects, etc.")
	.add(PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/infra"), "\t\t\t// All infra code: database, email service, etc.")
	.add(PathMatchers.hasRelativePathMatchingGlob(printedPath, "src/main/java/api"), "\t\t\t// All API code: controllers, etc.")
	.add(PathMatchers.hasNameMatchingGlob("*.properties"), "\t// Config file")
	.build();
	
var prettyPrinter = FileTreePrettyPrinter.builder()
	.customizeOptions(options -> options.withLineExtension(lineExtension))
	.build();
```
```
line_extension/
└─ src/
   └─ main/
      ├─ java/
      │  ├─ api/			// All API code: controllers, etc.
      │  │  └─ Controller.java
      │  ├─ domain/			// All domain code: value objects, etc.
      │  │  └─ ValueObject.java
      │  └─ infra/			// All infra code: database, email service, etc.
      │     └─ Repository.java
      └─ resources/
         └─ application.properties	// Config file
```

## Compact directories
Directories chain with single directory child are fully expanded by default, but you can compact them into a single tree entry.

```java
// Example: CompactDirectories.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withCompactDirectories(true))
    .build();
```
```
single_directory_child/
├─ file1
├─ file2
└─ this/is/single/directory/child/
   ├─ file1
   ├─ file2
   └─ file3
```

## Max depth
You can customize the default max depth (default is 20).

```java
// Example: MaxDepth.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withMaxDepth(3))
    .build();
```
```
max_depth/
└─ level1/
   ├─ file1#1
   ├─ file1#2
   └─ level2/
      ├─ file2#1
      ├─ file2#2
      └─ level3/
         └─ ... (max depth reached)
```

## Tree format
Choose between different tree formats.
The default is `UNICODE_BOX_DRAWING`, supported by all terminals, but you can also switch to use `CLASSIC_ASCII`.

```java
// Example: FileTreeFormat.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withTreeFormat(TreeFormat.CLASSIC_ASCII))
    .build();
```

```
tree_format/
|-- file_1
|-- file_2
`-- subFolder/
    |-- subFile_1
    `-- subFile_2
```

> [!TIP]
> *Idea for a future version: option to allow usage of custom format*

# Changelog
See [CHANGELOG.md](CHANGELOG.md) for released versions.

# Roadmap
See [ROADMAP.md](ROADMAP.md) for upcoming features.

# License
This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

# Contributing & Contact
For any questions or feedback, please open an issue on this repository.
	
