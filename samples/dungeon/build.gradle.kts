plugins {
    kotlin("jvm")
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(path = ":kfin", configuration = "jvmRuntimeElements"))
    implementation(project(path = ":graph", configuration = "jvmRuntimeElements"))
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test-junit"))
}

application {
    mainClass.set("com.toxicbakery.sample.dungeon.ApplicationKt")
}
