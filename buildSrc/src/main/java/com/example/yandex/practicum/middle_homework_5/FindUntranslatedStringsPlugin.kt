package com.example.yandex.practicum.middle_homework_5

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FilenameFilter
import javax.xml.parsers.DocumentBuilderFactory


class FindUntranslatedStringsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register(TASK_NAME, FindUntranslatedStringsTask::class.java)
    }

    companion object {
        private const val TASK_NAME = "untranslatedStrings"
    }
}

abstract class FindUntranslatedStringsTask : DefaultTask() {
    @TaskAction
    fun findUntranslatedStrings() {
        val resDir = File(project.projectDir, DIR_PATH)
        val baseStrings = File(resDir, RES_DIR)

        val stringValues = parseStrings(baseStrings)

        val dirFilter = FilenameFilter { dir, name ->
            val file = File(dir, name)
            file.isDirectory && file.name.startsWith("values-")
        }

        val missingTranslations = mutableMapOf<String, List<String>>()

        val localizedDirs = resDir.listFiles(dirFilter)
        if (localizedDirs.isNullOrEmpty() || stringValues.isEmpty()) {
            throw GradleException(String.format(NO_RES_DIRS_MSG, project.name))
        }

        localizedDirs?.forEach { directory ->
            val translationsFile = File(directory, RES_FILE_NAME)
            if (translationsFile.exists()) {
                val translationStrings = parseStrings(translationsFile)
                val untranslatedStrings = mutableListOf<String>()
                untranslatedStrings.addAll(stringValues.subtract(translationStrings))
                if (untranslatedStrings.isNotEmpty()) {
                    missingTranslations[directory.absolutePath] = untranslatedStrings
                }
            } else {
                missingTranslations[directory.absolutePath] = stringValues
            }
        }

        if (missingTranslations.isNotEmpty()) {
            val errorMessages = getMissingTranslationsMessages(missingTranslations)
            throw GradleException(errorMessages)
        }
    }

    private fun getMissingTranslationsMessages(missingTranslations: Map<String, List<String>>) : String {
        val stringBuilderErrorText = StringBuilder(ERROR_MSG).append(System.lineSeparator())
        missingTranslations.forEach { missing ->
            stringBuilderErrorText
                .append("=== ${missing.key} ===")
                .append(System.lineSeparator())
                .append(missing.value.joinToString(separator = System.lineSeparator()))
                .append(System.lineSeparator())
        }
        return  stringBuilderErrorText.toString()
    }

    private fun parseStrings(file: File) : List<String> {
        val stringsFromXml = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(file)
            .getElementsByTagName(TAG)

        return stringsFromXml.let { nodeList ->
            (0 until nodeList.length).map {i ->
                val node = nodeList.item(i)
                val name = node.attributes?.getNamedItem(TAG)?.nodeValue ?: ""
                name
            }
        }
    }

    companion object {
        private const val TAG = "string"
        private const val RES_FILE_NAME = "strings.xml"
        private const val DIR_PATH = "src/main/res"
        private const val RES_DIR = "values/strings.xml"
        private const val ERROR_MSG = "Missing translations"
        private const val NO_RES_DIRS_MSG = "No localized resources found (no values-* directories) in %s"
    }
}