import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.compose)
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = Java.jvmTarget
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(libs.ksp.api)
                implementation(libs.kotlin.poet.ksp)
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(project(":design-system"))
            }
        }
    }
}

compose {
    kotlinCompilerPlugin.set(Compose.kotlinCompilerPlugin)
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
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}
