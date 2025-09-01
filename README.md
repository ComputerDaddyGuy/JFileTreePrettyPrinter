# JFileTreePrettyPrint

A lightweight Java library for printing directory structures in a clean, tree-like format (think Unix `tree` command).

- Print folder trees like Unix `tree`
- Choose ASCII or Unicode styles for tree rendering
- Emoji support 🎉
- Limit displayed children statically or dynamically

ℹ️ Was developed just for fun!

* [Import dependency](#import-dependency)
* [Usage](#usage)  
* [Options](#options)
  * [Tree format](#tree-format)
  * [Emojis ❤️](#emojis-%EF%B8%8F)
  * [Children limit](#children-limit)
* [Changelog](#changelog) 
* [Roadmap](#roadmap) 
* [License](#license) 
* [Contributing & Contact](#contributing--contact) 


# Import dependency
For Maven, import this dependency to your `pom.xml`:

```xml
<!-- Maven -->
<dependency>
  <groupId>com.github.computerdaddyguy</groupId>
  <artifactId>jfiletreeprettyprinter</artifactId>
  <version>0.1.0</version>
</dependency>
```

For Gradle:
```
// Gradle
implementation "com.github.computerdaddyguy:jfiletreeprettyprinter:0.1.0"
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

## Tree format
Choose between different tree formats.
The default is `UNICODE_BOX_DRAWING`, supported by all terminals, but you can also switch to use `CLASSIC_ASCII`.

```java
// Example: FileTreeFormat.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withTreeFormat(TreeFormat.CLASSIC_ASCII)) // UNICODE_BOX_DRAWING (default) or CLASSIC_ASCII
    .build();
```

<table>
    <tr>
        <td>Unicode box drawing</td>
        <td>Classic ASCII</td>
    </tr>
    <tr>
        <td>
            <code>tree_format/
├─ file_1
├─ file_2
└─ subFolder/
   ├─ subFile_1
   └─ subFile_2</code>
        </td>
        <td>
            <code>tree_format/
|-- file_1
|-- file_2
`-- subFolder/
    |-- subFile_1
    `-- subFile_2</code>
        </td>
    </tr>
</table>

💡 *Idea for a future version: option to allow usage of custom format*


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

💡 *Idea for a future version: option to allow custom emoji mapping*

## Children limit
You can set a fixed limit to the number of children displayed for each directory.

```java
// Example: ChildrenLimitStatic.java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withChildrenLimit(3))
    .build();
```

```
static_children_limit/
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

Or you can also set a limitation function, to dynamically choose the number of children displayed in each directory (for, say, avoid cluttering the whole console with known large folders like `node_modules` but continue to pretty print normally other folders).

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

# Changelog
See [CHANGELOG.md](CHANGELOG.md) for released versions.

# Roadmap
See [ROADMAP.md](ROADMAP.md) for upcoming features.

# License
This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.

# Contributing & Contact
For any questions or feedback, please open an issue on this repository.
	