# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---
## [0.0.5] - Unreleased

### Added
- New path predicates: `hasParentMatching`, `hasAncestorMatching`, `hasDirectChildMatching`, `hasDescendantMatching`, `hasSiblingMatching`

### Changed
- `PathUtils` removed, `PathPredicates`rework

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
