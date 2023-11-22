plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "com.android.geto.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "com.android.geto.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidHilt") {
            id = "com.android.geto.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("androidRoom") {
            id = "com.android.geto.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}