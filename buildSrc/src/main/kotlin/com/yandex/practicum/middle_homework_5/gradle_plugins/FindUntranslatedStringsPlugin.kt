package com.yandex.practicum.middle_homework_5.gradle_plugins

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class FindUntranslatedStringsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("untranslatedStrings", FindUntranslatedStringsTask::class.java)
    }
}

abstract class FindUntranslatedStringsTask : DefaultTask() {
    @TaskAction
    fun findUntranslatedStrings() {
        val resDir = File(project.projectDir, RESOURCES_DIR_PATH)
        val defaultStringsFile = File(resDir, DEFAULT_STRINGS_FILE_PATH)

        if (!defaultStringsFile.exists()) {
            logger.lifecycle("[FindUntranslatedStringsPlugin] Пропуск модуля: файл ${defaultStringsFile.path} не найден.")
            return
        }

        val defaultStrings = parseStrings(defaultStringsFile)
        val localizedDirs = resDir.listFiles()?.filter { it.isDirectory && it.name.startsWith("values-") } ?: emptyList()

        val missingTranslations = mutableMapOf<String, MutableList<String>>()
        localizedDirs.forEach { dir ->
            val locale = dir.name
            val localizedStringsFile = File(dir, STRINGS_FILE_NAME)
            if (!localizedStringsFile.exists()) {
                missingTranslations[locale] = defaultStrings.toMutableList()
            } else {
                val localizedStrings = parseStrings(localizedStringsFile)
                val missing = defaultStrings - localizedStrings
                if (missing.isNotEmpty()) {
                    missingTranslations[locale] = missing.toMutableList()
                }
            }
        }

        if (missingTranslations.isNotEmpty()) {
            val errorText = StringBuilder("[FindUntranslatedStringsPlugin] Отсутствующие переводы:").append(System.lineSeparator())
            missingTranslations.forEach { (locale, missing) ->
                errorText.append("=== $locale ===").append(System.lineSeparator())
                missing.forEach { errorText.append(it).append(System.lineSeparator()) }
            }
            throw GradleException(errorText.toString())
        }
    }

    private fun parseStrings(file: File): Set<String> {
        return try {
            val stringsFromXml = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(file)
                .getElementsByTagName(STRING_TAG_NAME)
            (0 until stringsFromXml.length).mapNotNull { i ->
                stringsFromXml.item(i).attributes?.getNamedItem(NAME_ATTRIBUTE)?.nodeValue
            }.toSet()
        } catch (e: Exception) {
            throw GradleException("[FindUntranslatedStringsPlugin] Ошибка парсинга: ${file.path}: ${e.message}", e)
        }
    }

    companion object {
        private const val RESOURCES_DIR_PATH = "src/main/res"
        private const val DEFAULT_STRINGS_FILE_PATH = "values/strings.xml"
        private const val STRINGS_FILE_NAME = "strings.xml"
        private const val STRING_TAG_NAME = "string"
        private const val NAME_ATTRIBUTE = "name"
    }
}
