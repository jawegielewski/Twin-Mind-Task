package pl.jawegiel.twinmindjakubwegielewski.repository

import android.graphics.Bitmap
import kotlinx.coroutines.flow.flow
import pl.jawegiel.twinmindjakubwegielewski.utility.UtilsBitmap
import pl.jawegiel.twinmindjakubwegielewski.utility.DaoSharedPrefs

class RepositorySharedPrefs {

    companion object {
        const val USER_PHOTO = "user_photo"
    }

    fun getBitmap(key: String, defValue: String): Bitmap? {
        val bitmapString = DaoSharedPrefs.getString(key, defValue)
        return UtilsBitmap.stringToBitmap(bitmapString)
    }
   
    fun setBitmap(key: String, bitmap: Bitmap) {
        val bitmapString = UtilsBitmap.bitmapToString(bitmap)
        DaoSharedPrefs.setString(key, bitmapString!!)
    }
}