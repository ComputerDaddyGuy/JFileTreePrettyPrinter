# Roadmap

## Initial version
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

## Next version(s)
- [x] Child limitation function helper
- [x] More default emojis
- [x] Filtering
- [x] Ordering

## Other ideas
- [ ] Use Github wiki to document options instead of readme
- [ ] Custom tree format option
- [ ] Custom emojis option
- [ ] Additional text on lines
- [ ] Color option
- [ ] Follow symlink option
- [ ] Refactor unit tests (custom assert?)

## Abandoned ideas
These ideas will likely not been implemented because they do not align with JFileTreePrettyPrint vision:
- File attributes LineRenderer (size, author, createAt, etc.)
- Print optional legend for symlink/other file types symbols (at the end of the tree)