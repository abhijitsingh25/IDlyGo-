package com.iiitdharwad.qrscanner.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.iiitdharwad.qrscanner.data.Student

object QRCodeParser {

    private val gson = Gson()

    /**
     * Parses QR code content and extracts student information
     * Supports two formats:
     * 1. JSON: {"name": "John Doe", "regNo": "CS2020001"}
     * 2. Text: "Name: John Doe, RegNo: CS2020001"
     */
    fun parseQRCode(qrContent: String): Student? {
        return try {
            // Try parsing as JSON first
            parseJsonFormat(qrContent) ?: parseTextFormat(qrContent)
        } catch (e: Exception) {
            null
        }
    }

    private fun parseJsonFormat(qrContent: String): Student? {
        return try {
            val studentData = gson.fromJson(qrContent, StudentData::class.java)
            if (studentData.name.isNotBlank() && studentData.regNo.isNotBlank()) {
                Student(
                    name = studentData.name.trim(),
                    regNo = studentData.regNo.trim()
                )
            } else {
                null
            }
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    private fun parseTextFormat(qrContent: String): Student? {
        return try {
            val parts = qrContent.split(",")
            var name = ""
            var regNo = ""

            for (part in parts) {
                val trimmedPart = part.trim()
                when {
                    trimmedPart.startsWith("Name:", ignoreCase = true) -> {
                        name = trimmedPart.substringAfter(":").trim()
                    }
                    trimmedPart.startsWith("RegNo:", ignoreCase = true) -> {
                        regNo = trimmedPart.substringAfter(":").trim()
                    }
                }
            }

            if (name.isNotBlank() && regNo.isNotBlank()) {
                Student(name = name, regNo = regNo)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Data class for JSON parsing
     */
    private data class StudentData(
        val name: String = "",
        val regNo: String = ""
    )

    /**
     * Validates if a string could be a valid QR code content
     */
    fun isValidQRContent(content: String): Boolean {
        return content.isNotBlank() && (
            content.contains("name", ignoreCase = true) || 
            content.contains("regNo", ignoreCase = true) ||
            content.startsWith("{")
        )
    }
}