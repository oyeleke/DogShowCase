package com.example.dogsshowcase.Util

import java.io.InputStreamReader

class MockResponseFileReader(path: String) {

    val content: String

    init {
        val inputStream =
            this.javaClass.classLoader?.getResourceAsStream(path) ?: throw IllegalArgumentException(
                "File Not Found $path"
            )
        content = InputStreamReader(inputStream).use { reader ->
            reader.readText()
        }
    }
}