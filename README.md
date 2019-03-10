# Kotlin Bukkit Starter

Install:

```cmd
git clone https://github.com/Mainyf/KotlinBukkitStarter
```

Import project to ide.

Usage:

build jar
```cmd
./gradlew build 
```

build include dependencies jar
```cmd
./gradlew allJar
```

build sources jar
```cmd
./gradlew sourceJar
```

publish sources,doc,original jar to maven repo(repo uri oneself set!)
```cmd
./gradlew publish
```

copy include dependcies jar to server plugins folder
```cmd
./gradlew copyToPluginFolder
```
