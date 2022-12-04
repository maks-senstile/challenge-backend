package com.senstile

import com.senstile.domain.exceptions.ObjectNotFoundException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

object ResourceLoader {

    fun getAsString(filename: String, charset: Charset = StandardCharsets.UTF_8): String {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(filename.trim('/'))?.bufferedReader(charset)?.use {
            it.readText()
        } ?: throw ObjectNotFoundException("Resource file '$filename' not found.")
    }

    fun getAsBytes(filename: String): ByteArray {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(filename.trim('/'))?.use {
            it.readBytes()
        } ?: throw ObjectNotFoundException("Resource file '$filename' not found.")
    }
}