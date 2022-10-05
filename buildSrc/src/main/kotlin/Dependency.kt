object Dependency {
    object Kotlin {
        const val Version = "1.7.10"
    }

    object Paper {
        const val Version = "1.19"
        const val API = "1.19"
    }

    object Libraries {
        private const val monun = "io.github.monun"

        val Lib = arrayListOf(
            "${monun}:kommand-api:2.14.0"
        )

        val LibImpl = arrayListOf(
            "org.reflections:reflections:0.10.2",
            "com.google.code.gson:gson:2.9.1"
        )

        val LibCore = arrayListOf(
            "${monun}:kommand-core:2.14.0"
        )
    }
}