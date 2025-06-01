package com.yandex.practicum.middle_homework_5.gradle_plugins

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.LinkedList
import javax.xml.parsers.DocumentBuilderFactory

open class FindUntranslatedStringsTask : DefaultTask() {

    @TaskAction
    fun findUntranslatedStrings() {
        val resDir = File(project.projectDir, "src/main/res")
        val mainStringsFile = File(resDir, "values/strings.xml")

        val mainStrings = if (mainStringsFile.exists() && mainStringsFile.isFile) {
            readStrings(mainStringsFile)
        } else {
            return
        }

        val missingStringsByFile = mutableMapOf<String, List<String>>()

        resDir
            .listFiles { file, name -> file.isDirectory && name.startsWith("values-") }
            .orEmpty()
            .forEach { valuesDir ->
                val stringsFile = File(valuesDir, "strings.xml")

                if (!stringsFile.exists() || !stringsFile.isFile) {
                    return@forEach
                }

                val localizedStrings = readStrings(stringsFile).toSet()
                val missingStrings = LinkedList<String>()

                mainStrings.forEach { str ->
                    if (str !in localizedStrings) {
                        missingStrings.add(str)
                    }
                }

                if (missingStrings.isNotEmpty()) {
                    missingStringsByFile[stringsFile.absolutePath] = missingStrings
                }
            }

        if (missingStringsByFile.isNotEmpty()) {
            val message = composeErrorMessage(missingStringsByFile)
            throw GradleException(message)
        }
    }

    private fun readStrings(file: File): List<String> {
        val nodes = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(file)
            .getElementsByTagName("string")

        return (0 until nodes.length).map { index ->
            val node = nodes.item(index)
            node.attributes.getNamedItem("name")?.nodeValue.orEmpty()
        }
    }

    private fun composeErrorMessage(missingStringsByFile: Map<String, List<String>>) = buildString {
        appendLine("Missing translations:")

        missingStringsByFile.forEach { (file, missingStrings) ->
            appendLine()
            appendLine("In file $file:")
            missingStrings.forEach(::appendLine)
        }
    }
}