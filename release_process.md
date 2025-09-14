# JFileTreePrettyPrinter release flow

- **In `develop` branch:**
  - Run tests
  - Ensure `develop` branch is [Sonar-ready](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter&branch=develop)

- **Merge `develop` into `main` branch**

- **In `main` branch:**
  - Update `pom.xml` to remove `-SNAPSHOT` version (keep only `X.Y.Z`)
  - Update `README.md` (with new `X.Y.Z` dependency version)
  - Update `CHANGELOG.md` with changes
  - Update `ROADMAP.md` if necessary
  - Commit locally
  - Tag with appropriate `vX.Y.Z`
  - Push code & tags

- **In Github:**
  - Create new release based on tag: copy content of `CHANGELOG.md` for this version
  - Github `release` workflow will run automatically
  - Few minutes later, artifact is available on Maven Central 🎉

- **Merge `main` back into `develop` branch**