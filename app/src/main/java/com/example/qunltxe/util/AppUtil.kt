package com.example.qunltxe.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class AppUtil {
    companion object {
        fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            return Bitmap.createScaledBitmap(bmp, 1000, 1000, false)
        }
    }
}