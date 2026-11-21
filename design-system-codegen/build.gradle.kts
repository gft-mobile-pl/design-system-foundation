import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.maven.publish)
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(Java.jvmTarget))
        }
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        jvmMain.dependencies {
            implementation(libs.ksp.api)
            implementation(libs.kotlin.poet.ksp)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(project(":design-system"))
        }
    }
}

mavenPublishing {
    coordinates(project.property("libraryGroupId") as String, "design-system-codegen", project.property("libraryVersion") as String)

    pom {
        name.set(project.property("libraryCodegenName") as String)
        description.set(project.property("libraryCodegenDescription") as String)
        inceptionYear.set(project.property("libraryCodegenInceptionYear") as String)
        url.set("https://${project.property("libraryRepositoryUrl") as String}")
        licenses {
            license {
                name.set(project.property("libraryLicenseName") as String)
                url.set(project.property("libraryLicenseUrl") as String)
                distribution.set(project.property("libraryLicenseDistribution") as String)
            }
        }
        developers {
            developer {
                name.set(project.property("libraryDeveloperName") as String)
            }
        }
        scm {
            url.set("https://${project.property("libraryRepositoryUrl") as String}")
            connection.set("scm:git:git://${project.property("libraryRepositoryUrl") as String}")
            developerConnection.set("scm:git:ssh://git@${project.property("libraryRepositoryUrl") as String}.git")
        }
    }
    publishToMavenCentral()
    signAllPublications()
}
