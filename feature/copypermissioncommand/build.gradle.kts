plugins {
    alias(libs.plugins.com.android.geto.feature)
    alias(libs.plugins.com.android.geto.libraryCompose)
    alias(libs.plugins.com.android.geto.libraryJacoco)
}

android {
    namespace = "com.feature.copypermissioncommand"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}