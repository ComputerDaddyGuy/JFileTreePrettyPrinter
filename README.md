# JFileTreePrettyPrint

A lightweight Java library for printing directory structures in a clean, tree-like format (think Unix `tree` command).

ℹ️ Was developped just for fun!

# Usage

## Import lib


## Create a FileTreePrettyPrinter
```java
var printer = FileTreePrettyPrinter.createDefault();
var tree = printer.prettyPrint("src/example/resources/base");
System.out.println(tree);
```

Results:
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
Choose between different tree format.
The default is `UNICODE_BOX_DRAWING`, supported by all terminals, but you can also switch to use `CLASSIC_ASCII`.

```java
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
            <code>base/
  ├─ file1
  ├─ file2
  └─ subFolder/
     ├─ subFile1
     └─ subFile2</code>
        </td>
        <td>
            <code>base/
  |-- file1
  |-- file2
  `-- subFolder/
      |-- subFile1
      `-- subFile2</code>
        </td>
    </tr>
</table>

*💡Idea for a future version: allow custom format option*


## Emojis ❤️
If your terminal supports them, you can choose to use emojis.
Folders will have the "📂" emoji, and files will have an emoji depending on their extension.

```java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withEmojis(true))
    .build();
```

```
TODO: example
```

*💡Idea for a future version: allow custom format option*

## Children limit
You can set a fixed limit to the number of children displayed for each directory.

```java
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withChildrenLimit(1))
    .build();
```

```
base/
├─ businessPlan.pdf
└─ ... (3 files and 2 directories skipped)
```

Or you can also set a function, to dynamically choose the number of children displayed in each directory (for, say, avoid cluttering the whole console with known large folders like `node_modules`).
```java
Function<Path, Integer> pathLimitFunction = path -> path.getFileName().toString().equals("node_modules") ? 0 : -1; // Negative value means "no limit"
var prettyPrinter = FileTreePrettyPrinter.builder()
    .customizeOptions(options -> options.withChildrenLimitFunction(pathLimitFunction)) 
    .build();
```
```
dynamic_children_limit_function/
├─ cars/
│  ├─ Ferrari.doc
│  └─ Porsche.doc
└─ node_modules/
   └─ ... (9 files skipped)
```

# Changelog
See [CHANGELOG.md](CHANGELOG.md) for released versions.

# Roadmap
See [ROADMAP.md](ROADMAP.md) for upcoming features.

