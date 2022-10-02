import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.incremental.deleteRecursivelyOrThrow

plugins {
    idea
    kotlin("jvm") version Dependency.Kotlin.Version
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:${Dependency.Paper.Version}-R0.1-SNAPSHOT")
    Dependency.Libraries.Lib.forEach { compileOnly(it) }
}

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
            |main: ${rootProject.properties["group"]}.${codeName.joinToString("")}.plugin.${codeName.joinToString { it.capitalize() }}PluginMain
            |api-version: ${Dependency.Paper.API}
            |libraries: 
            ${Dependency.Libraries.LibCore.joinToString("\n") { "| - $it" }}
        """.trimMargin())
    }
    fun registerJar(
        classifier: String,
        source: Any,
        destination: Any?
    ) = register<Copy>("${classifier}Jar") {
        dependsOn(rootProject.tasks.findByName("generatePluginYml"))

        if (destination == null) {
            val prefix = rootProject.name
            val plugins = rootProject.file(".server/plugins")
            val update = File(plugins, "update")
            val regex = Regex("($prefix).*(.jar)")

            from(source)
            into(if (plugins.listFiles { _, it -> it.matches(regex) }?.isNotEmpty() == true) update else plugins)

            doLast {
                update.mkdirs()
                File(update, "RELOAD").delete()
            }
        }
        else {
            from(source)
            into(destination)
        }
    }

    registerJar("paper", jar, null)
    registerJar("output", jar, rootProject.file("out"))
}

idea {
    module {
        excludeDirs.addAll(listOf(file(".server"), file("out")))
    }
}