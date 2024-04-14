package com.example.demoimagecaching.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoimagecaching.model.DataRepository
import com.example.demoimagecaching.model.PhotosModel
import com.example.demoimagecaching.model.cache.LocalCachePreference
import com.example.demoimagecaching.model.cache.ImageCaching
import com.example.demoimagecaching.utils.InternetUtils
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModal(private val dataRepository: DataRepository = DataRepository()) : ViewModel() {
    var photosListData = mutableStateOf(emptyList<String>())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val cacheImageList = LocalCachePreference.getFormattedCacheImageListFromPreference() ?: mutableListOf()
            //Check If Images is already Cached So assign photosListData from LocalCachePreference list.
            if(cacheImageList.isNotEmpty()){
                withContext(Dispatchers.Main){
                    println("ImageGetFrom:- " + "LocalCache")
                    photosListData.value = cacheImageList
                }
            }

            //Another case if LocalCachePreference doesn't have any cache image list then fetch from internet.
            if(InternetUtils.isInternetAvailableOnDevice()) {
                val photos = getPhotosList()

                //This case is handle if photos fetched from API there size is equal to the LocalCachePreference image list then we assume both have same data so do not go to download images again or re-write it in disk space.
                if(cacheImageList.isNotEmpty() && cacheImageList.size == photos.size) return@launch

                //If Fetched Image List from API images does not exist in disk space , so download it and save into disk space for next time retrieval.
                for (i in photos.indices){
                    ImageCaching.getBitmap(photos[i].id , photos[i].urls?.regular)
                }
                if(ImageCaching.imageList.isNotEmpty()){
                    LocalCachePreference.setCacheImageListInPreference(Gson().toJson(ImageCaching.imageList))
                }
                println("ImageGetFrom:- " + "API Call Data")
                photosListData.value = ImageCaching.imageList
            }
        }
    }
    private suspend fun getPhotosList() : List<PhotosModel>{
        return dataRepository.getPhotosListData()
    }
}