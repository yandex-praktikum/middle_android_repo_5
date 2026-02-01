plugins {
    id("android-compose-library")
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.yandex.practicum.middle_homework_5.news"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.paging.compose)
    implementation(libs.goodle.gson)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    kapt(libs.room.kapt)
    implementation(libs.squareup.retrofit2)
    implementation(libs.koin.compose)
    implementation(libs.androidx.work.manager.ktx)
    debugImplementation(libs.androidx.ui.tooling)

    implementation(project(":settings"))
}
