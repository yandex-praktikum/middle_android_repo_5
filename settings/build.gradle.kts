plugins {
    id("android-compose-library")
}

android {
    namespace = "com.yandex.practicum.middle_homework_5.settings"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.data.store)
    implementation(libs.koin.compose)
    debugImplementation(libs.androidx.ui.tooling)
}
