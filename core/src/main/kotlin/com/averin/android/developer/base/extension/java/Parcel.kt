package com.averin.android.developer.base.extension.java

import android.os.Parcel

fun Parcel.writeIntList(input: List<Int>?) {
    input ?: return
    writeInt(input.size) // Save number of elements.
    input.forEach(this::writeInt) // Save each element.
}

fun Parcel.readIntList(): List<Int> {
    val size = readInt()
    val output = ArrayList<Int>(size)
    for (i in 0 until size) {
        output.add(readInt())
    }
    return output
}
