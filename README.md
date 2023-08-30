# modify gradle compiler args

A simple java-agent makes your Gradle journey much more fluent!

1. replace `--release` to `-source and -target` when using gradle.
2. flush for every PipedOutputStream write.

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

### 1. replace `--release`

When `java.toolchain.languageVersion` is set to a non-null value in `build.gradle`, the argument `--source` will be added to the compiler arguments.  
If you need to use some jdk internal classes like `--add-exports=java.base/jdk.internal.misc=xxx`, the compiler will tell you that you cannot export the packages when using `--release`.

This java-agent removes `--release` argument and place the value into `-source` and `-target` arguments, so that you can finish your build.

### 2. flush PipedOutputStream

Gradle uses PipedOutputStream for forwarding `stdin` to application in `JavaExec` tasks.  
However PipedOutputStream doesn't auto-flush the stream and you can only get data after 1 second.  
This java-agent automatically flushes the PipedOutputStream for every write, so that you can use `JavaExec` with `stdin` smoothly.
