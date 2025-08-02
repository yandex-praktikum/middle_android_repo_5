package com.yandex.practicum.middle_homework_5.gradle_plugins

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

abstract class FindUntranslatedStringsTask : DefaultTask() {
    @TaskAction
    fun findUntranslatedStrings() {
        val missingStrings = findMissingStrings()

        if (missingStrings.isNotEmpty()) {
            throw GradleException("Localization missing for: $missingStrings")
        }
    }

    private fun findMissingStrings(): Set<String> {
        val missingStringsSet = mutableSetOf<String>()
        var stringsSet: Set<String>? = null

        val resFile = File(project.projectDir, "src/main/res")
        val stringsFile = File(resFile, "values/strings.xml")
        resFile.listFiles { item ->
            (item?.isDirectory ?: false) && item.name.startsWith("values")
        }
            ?.forEach {
                val parsedStrings = getStringsSet(stringsFile)

                if (stringsSet == null) {
                    stringsSet = parsedStrings
                }

                if (stringsSet != parsedStrings) {
                    missingStringsSet.addAll(parsedStrings - stringsSet!!)
                    missingStringsSet.addAll(stringsSet!! - parsedStrings)
                }
            }
        return missingStringsSet
    }

    private fun getStringsSet(file: File): Set<String> {
        val stringsSet = mutableSetOf<String>()
        val stringNodes = getStringNodes(file)

        for (i in 0 until stringNodes.length) {
            val node: Node = stringNodes.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                val name = element.getAttribute("name")
                stringsSet.add(name)
            }
        }
        return stringsSet
    }

    private fun getStringNodes(file: File): NodeList =
        DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(file)
            .getElementsByTagName("string")
}

