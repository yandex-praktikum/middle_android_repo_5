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
        create("find-untranslated-strings") {
            id = "find-untranslated-strings"
            implementationClass = "com.yandex.practicum.middle_homework_5.gradle_plugins.FindUntranslatedStringsPlugin"
        }
    }
}