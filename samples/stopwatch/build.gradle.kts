plugins {
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(path = ":kfin", configuration = "jvmRuntimeElements"))
    implementation(project(path = ":graph", configuration = "jvmRuntimeElements"))
    implementation(kotlin("stdlib"))
    implementation("io.reactivex.rxjava2:rxjava:2.1.9")

    testImplementation(kotlin("test-junit"))
}
