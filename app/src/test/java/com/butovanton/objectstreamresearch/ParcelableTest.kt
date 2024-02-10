package com.butovanton.objectstreamresearch

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import org.junit.Assert
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

    data class MyClass(
        val data: String
    ) : Parcelable {
        override fun describeContents(): Int {
            return 0 //Parcelable.CONTENTS_FILE_DESCRIPTOR
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(data)
        }

        companion object {
            val creator = object : Creator<MyClass> {
                override fun createFromParcel(source: Parcel): MyClass {
                    return MyClass(source.readString() ?: throw IllegalArgumentException())
                }

                override fun newArray(size: Int): Array<MyClass> {
                    return newArray(size)
                }
            }
        }
    }

    @Test
    fun `pass myClass throw Intent`() {
       val parcel = Parcel.obtain()
       MyClass("data").writeToParcel(parcel, 0)
       parcel.setDataPosition(0)
       val newMyClass = MyClass.creator.createFromParcel(parcel)
       Assert.assertEquals(newMyClass.data, "data")
    }
}

