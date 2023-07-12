# modify gradle compiler args

A simple java-agent helps you replace `--release` to `-source and -target` when using gradle.

## How to use?

### 1. compile

`./gradlew clean jar`

### 2. copy to gradle folder

Copy `agent/build/libs/modify-gradle-compiler-args-agent.jar` to `~/.gradle/`

### 3. modify gradle.properties

```
org.gradle.jvmargs=--add-opens java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED --add-opens java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED -javaagent:../../modify-gradle-compiler-args-agent.jar
```

### 4. stop old daemon

`./gradlew --stop`

### 5. enjoy!

`./gradlew compileJava`

## Use case

When `java.toolchain.languageVersion` is set to a non-null value in `build.gradle`, the argument `--source` will be added to the compiler arguments.  
If you need to use some jdk internal classes like `--add-exports=java.base/jdk.internal.misc=xxx`, the compiler will tell you that you cannot export the packages when using `--release`.

This java-agent removes `--release` argument and place the value into `-source` and `-target` arguments, so that you can finish your build.
