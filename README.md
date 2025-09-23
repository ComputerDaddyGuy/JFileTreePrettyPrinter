# JFileTreePrettyPrint
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=alert_status&token=42442b67d269c6a17b4578ba2d87731c92b8922a)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=security_rating&token=42442b67d269c6a17b4578ba2d87731c92b8922a)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=vulnerabilities&token=42442b67d269c6a17b4578ba2d87731c92b8922a)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.computerdaddyguy/jfiletreeprettyprinter.svg?color=blue)](https://maven-badges.herokuapp.com/maven-central/io.github.computerdaddyguy/jfiletreeprettyprinter/)
[![Javadoc](https://javadoc.io/badge2/io.github.computerdaddyguy/jfiletreeprettyprinter/javadoc.svg?color=blue)](https://javadoc.io/doc/io.github.computerdaddyguy/jfiletreeprettyprinter)
[![Apache License 2.0](https://img.shields.io/:license-Apache%20License%202.0-blue.svg)](https://github.com/computerdaddyguy/jfiletreeprettyprinter/blob/main/LICENSE)

**A lightweight Java library for printing directory structures in a clean, tree-like format.**
- Sorting & filtering
- Emoji support 🎉
- Limit displayed children (fixed value or dynamically)
- Compact directory chains
- Maximum depth
- Various styles for tree rendering

> [!CAUTION]
> This lib was developed just for fun, and has not been thoroughly tested!  
> May not be suitable for production code 😊

<p align="center">
	<img src="https://github.com/ComputerDaddyGuy/JFileTreePrettyPrinter/blob/develop/assets/basic_usage.jpg?raw=true" alt="JFileTreePrettyPrint basic usage"/>
</p>

* [Import dependency](#import-dependency)
* [Usage](#usage)  
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
  <version>0.0.3</version>
</dependency>
```

For Gradle:
```
implementation "io.github.computerdaddyguy:jfiletreeprettyprinter:0.0.3"
```

# Usage
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

* [Tree format](#tree-format)
* [Emojis ❤️](#emojis-%EF%B8%8F)
* [Child limit](#child-limit)
* [Compact directories](#compact-directories)
* [Max depth](#max-depth)
* [Sorting](#sorting)
* [Filtering](#filtering)
  
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
You can set a fixed limit to the number of children displayed for each directory.

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
│  └─ ... (2 files skipped)
├─ folder_2/
│  ├─ file_2_1
│  ├─ file_2_2
│  ├─ file_2_3
│  └─ ... (2 files skipped)
└─ ... (3 directories skipped)

```

Or you can also set a limitation function, to dynamically choose the number of children displayed in each directory.
It avoids cluttering the whole console with known large folders (e.g. `node_modules`) but continue to pretty print normally other folders.

Use the `ChildLimitBuilder` and `PathPredicates` classes to help you build the limit function that fits your needs..

```java
// Example: ChildLimitDynamic.java
var childLimit = ChildLimitBuilder.builder()
	.defaultLimit(ChildLimitBuilder.UNLIMITED)
	.limit(PathPredicates.hasName("node_modules"), 0)
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
   └─ ... (9 files skipped)
```

> [!TIP]
> *Idea for a future version: helper for custom basic functions (by name, prefix, regex, etc.)*

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

## Sorting
Files and directories can be sorted using a custom comparator (default is alphabetical order).
If the provided comparator considers two paths equal (i.e., returns `0`), an alphabetical comparator is applied as a tie-breaker to ensure consistent results across all systems.  

The `PrettyPrintOptions.Sorts` class provides a set of basic, ready-to-use comparators.

```java
// Example: Sorting.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.sort(PrettyPrintOptions.Sorts.DIRECTORY_FIRST))
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

## Filtering
Files and directories can be selectively included or excluded using a custom `Predicate<Path>`.

Filtering is **recursive by default**: directory's contents will always be traversed.
However, if a directory does not match and none of its children match, the directory itself will not be displayed.

The `PathPredicates` class provides several ready-to-use `Predicate<Path>` implementations for common cases, as well as a builder for creating more advanced predicates.

```java
// Example: Filtering.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.filter(PathPredicates.hasExtension("java")))
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

# Changelog
See [CHANGELOG.md](CHANGELOG.md) for released versions.

# Roadmap
See [ROADMAP.md](ROADMAP.md) for upcoming features.

# License
This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

# Contributing & Contact
For any questions or feedback, please open an issue on this repository.
	
