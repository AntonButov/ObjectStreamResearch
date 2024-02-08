package com.butovanton.objectstreamresearch

import org.junit.Test

import org.junit.Assert.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.HashSet

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

    @Test
    fun `deserialization bomb`() {
        val root = HashSet<Any>()
        var s1 = root
        var s2 = HashSet<Any>()

        (0..100).forEach {
            val t1 = HashSet<Any>()
            val t2 = HashSet<Any>()
            t1.add("foo")
            s1.add(t1)
            s1.add(t2)
            s2.add(t1)
            s2.add(t2)
            s1 = t1
            s2 = t2
        }
        val bufferedOutputStream = ByteArrayOutputStream()
        ObjectOutputStream(bufferedOutputStream).apply {
            writeObject(root)
            println(bufferedOutputStream.toString())
            close()
        }
        ObjectInputStream(ByteArrayInputStream(bufferedOutputStream.toByteArray())).apply {
            val newExample = readObject() as HashSet<Any>
        }
    }
}