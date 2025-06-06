package pl.jawegiel.twinmindjakubwegielewski.utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


class UtilsBitmap {

    companion object {

        fun bitmapToString(bitmap: Bitmap): String? {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val bitmapEncoded: String? = Base64.encodeToString(byteArray, Base64.DEFAULT)
            return bitmapEncoded
        }

        fun stringToBitmap(encodedString: String?): Bitmap? {
            try {
                val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
                return bitmap
            } catch (e: Exception) {
                e.message
                return null
            }
        }
    }
}