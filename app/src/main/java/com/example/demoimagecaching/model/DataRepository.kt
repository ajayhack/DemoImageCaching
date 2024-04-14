package com.example.demoimagecaching.model

import com.example.demoimagecaching.model.webservice.WebService

class DataRepository(private var webService: WebService = WebService()) {
    suspend fun getPhotosListData() : MutableList<PhotosModel> {
        return webService.getPhotosData()
    }
}