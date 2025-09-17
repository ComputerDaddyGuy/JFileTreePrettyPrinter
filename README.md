# JFileTreePrettyPrint
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ComputerDaddyGuy_JFileTreePrettyPrinter&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter)

A lightweight Java library for printing directory structures in a clean, tree-like format.
- Various styles for tree rendering
- Emoji support 🎉
- Limit displayed children (fixed value or dynamically)
- Compact directory chains
- Maximum depth
- Sorting

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
  <version>0.0.2</version>
</dependency>
```

For Gradle:
```
implementation "io.github.computerdaddyguy:jfiletreeprettyprinter:0.0.2"
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

# Options

* [Tree format](#tree-format)
* [Emojis ❤️](#emojis-%EF%B8%8F)
* [Children limit](#children-limit)
* [Compact directories](#compact-directories)
* [Max depth](#max-depth)
* [Sorting](#sorting)
  
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

## Children limit
You can set a fixed limit to the number of children displayed for each directory.

```java
// Example: ChildrenLimitStatic.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withChildrenLimit(3))
    .build();
```

```
children_limit_static/
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

The `PathPredicates` class contains several ready-to-use predicates to help you.

```java
// Example: ChildrenLimitDynamic.java
Function<Path, Integer> pathLimitFunction = path -> PathPredicates.hasName(path, "node_modules") ? 0 : -1; // Negative value means "no limit"
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withChildrenLimitFunction(pathLimitFunction)) 
    .build();
```
```
children_limit_dynamic/
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
    .customizeOptions(options -> options.withFileSort(PrettyPrintOptions.Sorts.DIRECTORY_FIRST))
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

# Changelog
See [CHANGELOG.md](CHANGELOG.md) for released versions.

# Roadmap
See [ROADMAP.md](ROADMAP.md) for upcoming features.

# License
This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

# Contributing & Contact
For any questions or feedback, please open an issue on this repository.
	
