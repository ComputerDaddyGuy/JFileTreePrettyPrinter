# Roadmap

## Done
- [x] **Features**
  - [x] Option: filtering
  - [x] Option: ordering
  - [x] Option: emojis
  - [x] Option: compact directories display
  - [x] Option: line extension (=additional text after the file name)
  - [x] Option: children limit (static & dynamic)
  - [x] Option: tree format Unicode box drawing / classic ASCII
  - [x] Option: max directory depth
- [x] **Documentation**
  - [x] Add examples & README
  - [x] Use Github wiki to document options instead of readme
  - [x] Complete example with JFileTreePrettyPrint own project
- [x] **Code style**
  - [x] Use JSpecify annotations
- [x] **Testing**
  - [x] Unit tests, using @TempDir
  - [x] Jacoco coverage report
  - [x] Mutation testing
  - [x] SonarCloud integration
- [x] **Workflows**
  - [x] Github actions
  - [x] Publish on Maven Central!

## To do
- [x] More `PathMatchers` functions!
- [x] Helper class for line extension
- [x] Helper class for sorting
- [ ] Option: custom emojis
- [ ] Option: hide number of skipped files and folders for child limit
- [ ] Rework/fix Github wiki to be up to date

## Backlog / To analyze / To implement if requested
- [ ] Option: custom tree format
- [ ] Refactor unit tests (custom assert?)
- [ ] Option: color
- [ ] Option: Filename decorator
- [ ] Option: Follow symlink
- [ ] Advanced line extension function (file size, author, timestamps, etc. )
