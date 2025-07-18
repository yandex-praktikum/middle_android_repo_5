plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(gradleApi())
}
allprojects {
    repositories {
        mavenCentral()
    }
}

gradlePlugin {
    plugins {
        create("untranslated") {
            id = "com.example.yandex.practicum.middle_homework_5.untranslated"
            implementationClass =
                "com.example.yandex.practicum.middle_homework_5.FindUntranslatedStringsPlugin"
        }
    }
}