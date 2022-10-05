import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.incremental.deleteRecursivelyOrThrow

plugins {
    idea
    kotlin("jvm") version Dependency.Kotlin.Version
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:${Dependency.Paper.Version}-R0.1-SNAPSHOT")

    Dependency.Libraries.Lib.forEach { compileOnly(it) }
    Dependency.Libraries.LibImpl.forEach { implementation(it) }
}

val shade = configurations.create("shade")
shade.extendsFrom(configurations.implementation.get())

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }
    processResources {
        filesMatching("**/*.yml") {
            expand(project.properties)
        }
        filteringCharset = "UTF-8"
    }
    jar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set("")
    }
    register<Task>("generatePluginYml") {
        var newlyCretaed: Boolean
        val resourcesDir = rootProject.file("src/main/resources").also { if (!it.exists()) it.mkdirs(); newlyCretaed = true }
        val pluginYml = File(resourcesDir, "plugin.yml").also { if (!it.exists()) it.createNewFile() }

        if (!newlyCretaed) pluginYml.deleteRecursivelyOrThrow(); pluginYml.createNewFile()
        pluginYml.writeText("""
            |name: ${rootProject.pluginName}
            |version: ${rootProject.properties["version"]}
            |main: ${rootProject.properties["group"]}.${codeName.joinToString("")}.Main
            |api-version: ${Dependency.Paper.API}
            |libraries: 
            ${Dependency.Libraries.LibCore.joinToString("\n") { "| - $it" }}
        """.trimMargin())
    }

    register<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("paperJar") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set("")

        from(
            shade.map {
                if (it.isDirectory)
                    it
                else
                    zipTree(it)
            }
        )

        from(sourceSets["main"].output)

        doLast {
            copy {
                from(archiveFile)
                val plugins = File(rootDir, ".server/plugins/")
                into(plugins)
            }
        }
    }
}

idea {
    module {
        excludeDirs.addAll(listOf(file(".server"), file("out")))
    }
}