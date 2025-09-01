# Roadmap

## Next version
- [x] Use JSpecify annotations
- [x] Regroup all formatting work under single "LineFormatter"
- [x] Builder with options (depth symbols, emoji, child limit)
- [x] Unit tests, using @TempDir
- [x] Better "isLastChild" detection algorithm
- [x] Pre-defined Path predicates
- [x] Add examples & README
- [x] Implement emojis for files
- [ ] Max depth options
- [ ] Unify dir-in-a-row into a single entry option
- [ ] Publish on Maven Central!

## Other ideas
- [ ] Custom tree format option
- [ ] Custom emojis option
- [ ] Color option
- [ ] Filtering
- [ ] Follow symlink option
- [ ] Print optional legend for symlink/other file types symbols (at the end of the tree)
- [ ] File attributes LineRenderer (size, author, createAt, etc.)
- [ ] Alternative implementation option, just for fun (using DirectoryStream instead of Files.walkFileTree)
