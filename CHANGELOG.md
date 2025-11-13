# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---
## [0.2.0] - 2025-11-13 - Native executable 🎉

> [!IMPORTANT]
> Maven artifactId of Java lib changed from `jfiletreeprettyprinter` to `jfiletreeprettyprinter-core`

### Added
- Run JFileTreePrettyPrinter in command line, using native executable on Windows, Linux, MacOS (see Github release attachments):  
```bash
$ jfiletreeprettyprinter myFolder
```
- Supports options declared in external file:  
```bash
$ jfiletreeprettyprinter myFolder --options myOptions.json
```
- 👉 More docs in [README](https://github.com/ComputerDaddyGuy/JFileTreePrettyPrinter#native-cli)

### Changed
- Split code into 3 sub-modules: cli, core and examples

---
## [0.1.1] - 2025-11-08 - Native executable (POC)

> [!CAUTION]
> Do not use this version (was for proof of concept only).

---
## [0.1.0] - 2025-10-13 - First beta release

### Added
- Emojis: option to use custom mapping
- Tree format: option to use custom format

### Changed
- Child limit: do not print skipped children count
- Max depth: do not print "max depth reached"
- Refactor options packages

---
## [0.0.5] - 2025-10-04

### Added
- New various path matchers
- New `LineExtensions`,  `ChildLimits` and `PathSorts` helper classes (and associated builders)

### Changed
- Helpers classes `PathUtils` and `PathPredicates` removed, use `PathMatchers` instead
- Filtering: now using `PathMatcher` interface instead of `Predicate<Path>`
- Filtering: split into distinct directories and files filters for better control
- Line extension: empty string is now permitted

### Fixed
- The folder name is properly displayed at root when calling `prettyPrint(".")` (instead of "./")

---
## [0.0.4] - 2025-09-27

### Added
- Line extension: append any String after a path (comment, file details, etc.)

### Changed
- Filtering: moved to options
- Compact dirs: root dir is now never compacted

### Removed
- Error handling: exceptions are now thrown instead of being handled by the renderer

### Documentation
- Documentation moved to [Github wiki](https://github.com/ComputerDaddyGuy/JFileTreePrettyPrinter/wiki)

---
## [0.0.3] - 2025-09-21

### Added
- Filtering
- New `ChildLimitBuilder` helper class
- New `PathUtils` helper class

### Changed
- Sorting: option method `withFileSort` renamed in `sort`
- Child limit: various renaming and refactor

---
## [0.0.2] - 2025-09-16

### Added
- Option: sorting files and directories
- 39 new default files & extension mappings for emojis

---
## [0.0.1] - 2025-09-14

### Added
- Initial release
- Option: tree format Unicode box drawing / classic ASCII
- Option: use emojis
- Option: child limit (static & dynamic)
- Option: compact directories display
- Option: max directory depth
