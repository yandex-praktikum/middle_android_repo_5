package com.yandex.practicum.middle_homework_5.gradle_plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class FindUntranslatedStringsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.register("untranslatedStrings", FindUntranslatedStringsTask::class.java)
        target.tasks.findByName("preBuild")?.dependsOn("untranslatedStrings")
    }
}