# JFileTreePrettyPrinter release flow

- **In `develop` branch:**
  - Run tests
  - Ensure `develop` branch is [Sonar-ready](https://sonarcloud.io/summary/new_code?id=ComputerDaddyGuy_JFileTreePrettyPrinter&branch=develop)

- **Merge `develop` into `main` branch**
  - Note: `pom.xml` must have `X.Y.Z-SNAPSHOT` version

- **In `main` branch:**
  - Set project version in `pom.xml` to `X.Y.Z` (remove the `-SNAPSHOT`)
  - Update `README.md` (with new `X.Y.Z` dependency version)
  - Update `CHANGELOG.md` with changes **!IMPORTANT: Respect format!**
  - Update `ROADMAP.md` if necessary
  - Update `SECURITY.md` if necessary
  - Commit locally
  - Tag with appropriate `vX.Y.Z`
  - Push code & tags

- **In Github:**
  - Action `create-github-release` triggers automatically on tag push `vX.Y.Z` on `main` branch
    - Content matching of `CHANGELOG.md` for this version is used
  - Github `release` workflow will run automatically
  - Few minutes later, artifact is available on Maven Central 🎉
  - Update wiki if required

- **Merge `main` back into `develop` branch**
  - Note: `pom.xml` now has `X.Y.(Z+1)-SNAPSHOT` version