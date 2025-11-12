# How to build a native executable locally

## Windows
1. Install `GraalVM`: https://www.graalvm.org/downloads/
   1. Configure environment variable:
   ```
   GRAALVM_HOME=<graalvm home>
   GRAALVM_PATH=%GRAALVM_HOME%\bin
   ```
1. Install `Visual Studio Code`: https://visualstudio.microsoft.com/fr/downloads/
   1. IMPORTANT: Be sure to install `Desktop development with C++` to avoid `Failed to find a suitable version of Visual Studio with 'vswhere.exe'` error
1. Configure Maven plugin: https://graalvm.github.io/native-build-tools/latest/end-to-end-maven-guide.html#add-plugin
   1. Use `native` profile
   1. Set `native.maven.plugin.version` property to latest version from https://mvnrepository.com/artifact/org.graalvm.buildtools/native-maven-plugin
   1. Set appropriate main class
1. From the project root, run `mvn -Pnative package`
1. After a few seconds, `jfiletreeprettyprinter.exe` has been generated inside `target/` folder

### UTF-8 console
If printed tree format looks like `Ôöé` instead of `├─`, then your console is likely not configured to UTF-8 encoding.  
To configure your console to UTF-8 code page, run:
```
chcp 65001
```
More info on [chcp Windows command](https://learn.microsoft.com/en-us/windows-server/administration/windows-commands/chcp).

## Linux
No doc for now.

## MacOS
No doc for now.