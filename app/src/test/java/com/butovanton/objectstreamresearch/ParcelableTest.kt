package com.butovanton.objectstreamresearch

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import kotlinx.parcelize.Parcelize
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

    data class ClassHandleParcelable(
        val data: String
    ) : Parcelable {
        override fun describeContents(): Int {
            return 0 //Parcelable.CONTENTS_FILE_DESCRIPTOR
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(data)
        }

        companion object {
            @JvmField
            val CREATOR = object : Creator<ClassHandleParcelable> {
                override fun createFromParcel(source: Parcel): ClassHandleParcelable {
                    return ClassHandleParcelable(
                        source.readString() ?: throw IllegalArgumentException()
                    )
                }

                override fun newArray(size: Int): Array<ClassHandleParcelable> {
                    return newArray(size)
                }
            }
        }
    }

    @Test
    fun `pass myClass through parcel`() {
        val parcel = Parcel.obtain()
        ClassHandleParcelable("data").writeToParcel(parcel, 0)
        parcel.setDataPosition(0)
        val newClassHandleParcelable = ClassHandleParcelable.CREATOR.createFromParcel(parcel)
        Assert.assertEquals(newClassHandleParcelable.data, "data")
    }

    @Test
    fun `pass my class throw bundle`() {
        val parcel = Parcel.obtain()
        val classHandleParcelable = ClassHandleParcelable("data")
        val bundle = Bundle().also { it.putParcelable("key", classHandleParcelable) }
        parcel.writeBundle(bundle)
        parcel.setDataPosition(0)
        val newBundle = parcel.readBundle()
        val newClassHandleParcelable =
            newBundle?.getParcelable("key", ClassHandleParcelable::class.java)!!
        assert(newClassHandleParcelable.data == "data")
    }

    @Parcelize
    data class ParcelFromPlugin(
        val data: String
    ) : Parcelable

    @Test
    fun `test plugin`() {
        val parcel = Parcel.obtain()
        ParcelFromPlugin(data = "data").writeToParcel(parcel, 0)
        assert(parcel.dataPosition() > 0)
    }

    @Test
    fun `save and read bundle to parcel`() {
        val parcel = Parcel.obtain()
        val oldParcelFromPlugin = ParcelFromPlugin("data")
        val bundle = Bundle().also {
            it.putParcelable("key", oldParcelFromPlugin)
        }
        parcel.writeBundle(bundle)
        parcel.setDataPosition(0)
        val newBundle = parcel.readBundle()
        val newParcelFromPlugin = newBundle?.getParcelable("key", ParcelFromPlugin::class.java)
            ?: throw IllegalArgumentException()
        assert(newParcelFromPlugin.data == "data")
    }
}

