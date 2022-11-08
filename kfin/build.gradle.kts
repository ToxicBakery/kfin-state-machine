import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("multiplatform")
    id("maven-publish")
    id("signing")
    id("jacoco")
    id("org.jetbrains.dokka")
}

kotlin {
    jvm {
        withJava()
    }
    js {
        nodejs()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("reflect"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.10")
}

val jacocoTestReportJvm by tasks.register("jacocoTestReportJvm", JacocoReport::class) {
    classDirectories.from(
        files(
            "$buildDir/classes/kotlin/common/main", "$buildDir/classes/kotlin/jvm/main"
        )
    )
    sourceDirectories.from(
        files(
            "src/commonMain/kotlin",
            "src/jvmMain/kotlin"
        )
    )
    executionData.from(
        files(
            "$buildDir/jacoco/jvmTest.exec"
        )
    )

    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("${buildDir}/reports/jacoco/report.xml"))
        html.required.set(true)
    }
}

tasks.register("dokkaGhPages", DokkaTask::class) {
    outputDirectory.set(file("$projectDir/gh-pages"))
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets.all {
        sourceRoots.from(file("src/commonMain/kotlin"))
    }
}

val dokkaJavadocCommonJar by tasks.register("dokkaJavadocCommonJar", Jar::class.java) {
    archiveClassifier.set("javadoc")
    from("$buildDir/dokka/html")
}

val dokkaJavadocJvmJar by tasks.register("dokkaJavadocJvmJar", Jar::class.java) {
    archiveClassifier.set("javadoc")
    from("$buildDir/dokka/html")
}

val dokkaJavadocJsJar by tasks.register("dokkaJavadocJsJar", Jar::class.java) {
    archiveClassifier.set("javadoc")
    from("$buildDir/dokka/html")
}

val POM_DESCRIPTION: String by project
val POM_NAME: String by project
val POM_URL: String by project
val POM_SCM_URL: String by project
val POM_SCM_CONNECTION: String by project
val POM_SCM_DEV_CONNECTION: String by project
val POM_LICENCE_NAME: String by project
val POM_LICENCE_URL: String by project
val POM_LICENCE_DIST: String by project
val POM_DEVELOPER_ID: String by project
val POM_DEVELOPER_NAME: String by project
val POM_DEVELOPER_EMAIL: String by project
val POM_DEVELOPER_ORGANIZATION: String by project
val POM_DEVELOPER_ORGANIZATION_URL: String by project

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                description.set(POM_DESCRIPTION)
                name.set(POM_NAME)
                url.set(POM_URL)
                scm {
                    url.set(POM_SCM_URL)
                    connection.set(POM_SCM_CONNECTION)
                    developerConnection.set(POM_SCM_DEV_CONNECTION)
                }
                licenses {
                    license {
                        name.set(POM_LICENCE_NAME)
                        url.set(POM_LICENCE_URL)
                        distribution.set(POM_LICENCE_DIST)
                    }
                }
                developers {
                    developer {
                        id.set(POM_DEVELOPER_ID)
                        name.set(POM_DEVELOPER_NAME)
                        email.set(POM_DEVELOPER_EMAIL)
                        organization.set(POM_DEVELOPER_ORGANIZATION)
                        organizationUrl.set(POM_DEVELOPER_ORGANIZATION_URL)
                    }
                }
            }
        }
    }

    publications.getByName("js", MavenPublication::class) {
        artifact(dokkaJavadocJsJar)
    }
    publications.getByName("jvm", MavenPublication::class) {
        artifact(dokkaJavadocJvmJar)
    }
    publications.getByName("kotlinMultiplatform", MavenPublication::class) {
        artifact(dokkaJavadocCommonJar)
    }

    repositories {
        val releaseUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
        val snapshotUrl = "https://oss.sonatype.org/content/repositories/snapshots"

        maven {
            setUrl(if (!project.version.toString().contains("SNAPSHOT")) releaseUrl else snapshotUrl)
            credentials {
                username = System.getenv("SONATYPE_USERNAME") ?: ""
                password = System.getenv("SONATYPE_PASSWORD") ?: ""
            }
        }
    }
}

signing {
    isRequired = false
    sign(publishing.publications)
}

tasks.getByName("build").dependsOn(jacocoTestReportJvm)
tasks.getByName("publish").dependsOn("assemble")
tasks.getByName("publishToMavenLocal").dependsOn("assemble")
