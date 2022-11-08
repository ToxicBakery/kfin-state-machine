plugins {
    kotlin("multiplatform") version "1.7.10" apply false
    id("org.jetbrains.dokka") version "1.7.10" apply false
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        gradlePluginPortal()
    }
}

subprojects {
    val tagName = System.getenv("CIRCLE_TAG")
    val isTag = tagName != null && !tagName.isEmpty()
    val buildNumber = System.getenv("CIRCLE_BUILD_NUM") ?: "0"
    val tag = if (isTag) "" else "-SNAPSHOT"

    group = "com.ToxicBakery.kfinstatemachine"
    version = "5.0.$buildNumber$tag"

    tasks.withType(Test::class.java) {
        testLogging {
            showStandardStreams = true
            setEvents(listOf("passed", "failed"))
        }
    }
}
