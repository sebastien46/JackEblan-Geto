plugins {
    id("com.android.geto.library")
    id("com.android.geto.hilt")
    id("com.android.geto.room")
}

android {
    namespace = "com.core.database"

    defaultConfig {
        testInstrumentationRunner = "com.core.testing.HiltTestRunner"
    }
}

dependencies {
    implementation(project(":core:model"))
}