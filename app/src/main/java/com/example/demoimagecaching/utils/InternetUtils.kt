package com.example.demoimagecaching.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.demoimagecaching.model.cache.LocalCachePreference
import com.example.demoimagecaching.view.MyApplication

object InternetUtils {

    fun isInternetAvailableOnDevice(): Boolean {
        var isInternetAvailableOnDevice = true
        // Get Application Context
        try {
            val appContext: Context? = MyApplication.getAppContext()

            // Get ConnectivityManager
            val connectivityManager = appContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // Get NetworkInfo
            val networkInfo = connectivityManager.activeNetworkInfo
            //	networkInfo.
            isInternetAvailableOnDevice = !(networkInfo == null || !networkInfo.isConnected)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return isInternetAvailableOnDevice
    }

    fun noDataFoundCase() : Boolean {
        return !isInternetAvailableOnDevice() && LocalCachePreference.getCacheImageListInPreference().isNullOrEmpty()
    }
}