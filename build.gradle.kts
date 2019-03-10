@file:Suppress("UNCHECKED_CAST")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `kotlin-dsl`
    `maven-publish`
    kotlin("jvm") version "1.3.20"
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

ext {
    set("group", "io.github.mainyf")
    set("mainClass", "io.github.mainyf.demoplugin.Main")
    set("version", "1.0")
    set("javaVersion", "1.8")
    set("server", "./server")
    set("Authors", listOf("Mainyf"))
    set("mavenRepoPath", "")
}

group = ext["group"]!! as String
version = ext["version"]!! as String

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.toVersion(project.ext["javaVersion"]!!)
    targetCompatibility = JavaVersion.toVersion(project.ext["javaVersion"]!!)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = project.ext["javaVersion"]!! as String
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
    maven {
        name = "elmakers"
        url = uri("http://maven.elmakers.com/repository")
    }
}

dependencies {
    archives(kotlin("stdlib-jdk8"))
    compile("org.bukkit:craftbukkit:1.7.10-R0.1-SNAPSHOT")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    configurations.compile.get().extendsFrom(configurations.archives.get())
}

bukkit {
    name = project.name
    main = project.ext["mainClass"]!! as String
    version = project.version as String
    authors = ext["Authors"]!! as List<String>
}

val allJar by tasks.registering(Jar::class) {
    archiveClassifier.set("all")
    dependsOn("classes")
    from(
        sourceSets.main.get().output.classesDirs.files,
        sourceSets.main.get().output.resourcesDir,
        configurations.getByName("archives").map {
            if (it.isDirectory) it else zipTree(it)
        }
    )
}

val originalJar by tasks.registering(Jar::class) {
    archiveClassifier.set("original")
    dependsOn("classes")
    from(
        sourceSets.main.get().output.classesDirs.files,
        sourceSets.main.get().output.resourcesDir
    )
}

val copyToPluginFolder by tasks.registering(Copy::class) {
    from(allJar)
    into("${ext["server"]}/plugins/")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks["javadoc"])
}

val sourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            artifactId = project.name.toLowerCase()

            artifact(sourceJar.get())
            artifact(javadocJar.get())
        }
    }
    repositories {
        maven(url = project.ext["mavenRepoPath"]!!)
    }
}