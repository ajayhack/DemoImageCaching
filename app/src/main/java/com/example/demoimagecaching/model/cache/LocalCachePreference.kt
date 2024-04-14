package com.example.demoimagecaching.model.cache

import android.content.Context
import android.preference.PreferenceManager
import com.example.demoimagecaching.view.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val imagePrefListKey = "imagePrefListKey"

object LocalCachePreference {

    private fun writeContentToSharedPreferences(key: String?, value: Any?) {
        val context: Context? = MyApplication.getAppContext()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()

        when (value) {
            is String -> {
                editor.putString(key, value.trim { it <= ' ' })
            }

            is Boolean -> //Boolean value
            {
                editor.putBoolean(key, (value as Boolean?)!!)
            }

            is Float -> //Float value
            {
                editor.putFloat(key, (value as Float?)!!)
            }

            is Int -> //Integer value
            {
                editor.putInt(key, (value as Int?)!!)
            }

            is Long -> //Long value
            {
                editor.putLong(key, (value as Long?)!!)
            }
        }

        editor.commit()
    }

    private fun getContentFromSharedPreferences(dataKey: String, classType: Class<out Any>): Any? {
        val `object`: Any?

        //Get App Context
        val context = MyApplication.getAppContext()

        //Get SharedPreferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        //Get Content from File
        `object` = if (classType == String::class.java) //String value
        {
            sharedPreferences.getString(dataKey, "")
        } else if (classType == Boolean::class.java) //Boolean value
        {
            sharedPreferences.getBoolean(dataKey, false)
        } else if (classType == Int::class.java) //Integer value
        {
            sharedPreferences.getInt(dataKey, 0)
        } else if (classType == Float::class.java) //Float value
        {
            sharedPreferences.getFloat(dataKey, 0.00f)
        } else if (classType == Long::class.java) //Long value
        {
            sharedPreferences.getLong(dataKey, 0L)
        } else {
            null
        }
        return `object`
    }

    fun setCacheImageListInPreference(value: String?) {
        writeContentToSharedPreferences(imagePrefListKey, value)
    }

    fun getCacheImageListInPreference(): String? {
        return getContentFromSharedPreferences(imagePrefListKey, String::class.java) as? String
    }

    fun getFormattedCacheImageListFromPreference() : MutableList<String>?{
        val type = object : TypeToken<MutableList<String>?>() {}.type
        return if(Gson().fromJson<MutableList<String>>(getCacheImageListInPreference(), type) != null) {
            Gson().fromJson(getCacheImageListInPreference(), type)
        }else{
            mutableListOf()
        }
    }
}