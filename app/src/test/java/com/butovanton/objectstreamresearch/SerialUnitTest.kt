package com.butovanton.objectstreamresearch

import org.junit.Test

import org.junit.Assert.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

data class SomeClass(val data: String) : Serializable


class SerialUnitTest {
    @Test
    fun addition_isCorrect() {
        val bufferedOutputStream = ByteArrayOutputStream()
        ObjectOutputStream(bufferedOutputStream).apply {
            writeObject(SomeClass(data = "testData"))
            close()
        }
        ObjectInputStream(ByteArrayInputStream(bufferedOutputStream.toByteArray())).apply {
            val newExample = readObject() as SomeClass
            assertEquals("testData", newExample.data)
            println(bufferedOutputStream.toString())
            println(newExample)
            close()
        }
    }
}