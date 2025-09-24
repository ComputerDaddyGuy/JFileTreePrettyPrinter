# Roadmap

## Done
- [x] Option: tree format Unicode box drawing / classic ASCII
- [x] Option: use emojis
- [x] Option: children limit (static & dynamic)
- [x] Option: compact directories display
- [x] Option: max directory depth
- [x] Add examples & README
- [x] Use JSpecify annotations
- [x] Unit tests, using @TempDir
- [x] Mutation testing
- [x] Pre-defined Path predicates
- [x] Publish on Maven Central!
- [x] Child limitation function helper
- [x] More default emojis
- [x] Option: Filtering
- [x] Option: Ordering

## To do
- [x] Use Github wiki to document options instead of readme
- [x] Jacoco coverage report
- [ ] Option: Line extension (=additional text after the file name)
- [ ] Option: Filename decorator

## Backlog
- [ ] Option: custom tree format
- [ ] Option: custom emojis
- [ ] Option: color
- [ ] Refactor unit tests (custom assert?)
- [ ] More `PathPredicates` functions!

## Abandoned
These ideas will likely not been implemented because they do not align with JFileTreePrettyPrint vision:
- File attributes LineRenderer (size, author, createAt, etc.)
- Print optional legend for symlink/other file types symbols (at the end of the tree)
- Follow symlink option