import org.gradle.api.Project

val Project.pluginName
    get() = rootProject.name.split("-").joinToString("") { it.capitalize() }

val Project.codeName
    get() = rootProject.properties["codeName"].toString().split("-")