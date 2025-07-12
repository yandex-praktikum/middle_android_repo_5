package com.example.buildsrc

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

abstract class FindUntranslatedStringsTask : DefaultTask() {

    @TaskAction
    fun findUntranslatedStrings() {
        val resDir = File(project.projectDir, "src/main/res")
        val defaultStringsFile = File(resDir, "values/strings.xml")

        if (!defaultStringsFile.exists()) return

        val defaultStrings = parseStringResources(defaultStringsFile)

        val valuesDirs = resDir.listFiles()
            ?.filter { it.isDirectory && it.name.startsWith("values-") }
            ?: return

        val missingStrings = mutableMapOf<String, List<String>>()

        for (dir in valuesDirs) {
            val stringsFile = File(dir, "strings.xml")
            if (!stringsFile.exists()) continue

            val localizedStrings = parseStringResources(stringsFile)
            val missing = defaultStrings.filterNot { it in localizedStrings }

            if (missing.isNotEmpty()) {
                missingStrings[dir.name] = missing
            }
        }

        if (missingStrings.isNotEmpty()) {
            val stringBuilderErrorText = StringBuilder("Missing translations").appendLine()
            missingStrings.forEach { missing ->
                stringBuilderErrorText
                    .append("=== ${missing.key} ===").appendLine()
                    .append(missing.value.joinToString(System.lineSeparator())).appendLine()
            }
            throw GradleException(stringBuilderErrorText.toString())
        }
    }

    private fun parseStringResources(file: File): List<String> {
        val nodeList = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(file)
            .getElementsByTagName("string")

        return (0 until nodeList.length).map {
            val node = nodeList.item(it)
            node.attributes?.getNamedItem("name")?.nodeValue ?: ""
        }
    }
}