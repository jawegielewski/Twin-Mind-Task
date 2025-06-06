package pl.jawegiel.twinmindjakubwegielewski.utility

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object DaoSharedPrefs {

    private var sharedPrefs: SharedPreferences? = null
    private lateinit var prefsEditor: SharedPreferences.Editor

    fun init(context: Context) {
        if (sharedPrefs == null) sharedPrefs = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        prefsEditor = sharedPrefs!!.edit()
    }

    fun setString(key: String, value: String) {
        prefsEditor.putString(key, value).apply()
    }

    fun getString(key: String, defValue: String): String? {
        return sharedPrefs!!.getString(key, defValue)
    }
}