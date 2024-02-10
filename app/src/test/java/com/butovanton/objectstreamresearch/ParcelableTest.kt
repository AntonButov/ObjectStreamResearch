package com.butovanton.objectstreamresearch

import android.os.Parcel
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ParcelableTest {

    @Test
    fun `write to parcel and read`() {
        val byte: Byte = 1
        val long: Long = 2
        val string = "String"
        val parcel = Parcel.obtain()
        parcel.writeByte(byte)
        println("dataSize = ${parcel.dataSize()}")
        parcel.writeLong(long)
        println("dataSize = ${parcel.dataSize()}")
        parcel.writeString(string)
        println("dataSize = ${parcel.dataSize()}")
    }
}