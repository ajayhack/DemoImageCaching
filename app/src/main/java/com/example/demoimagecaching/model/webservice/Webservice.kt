package com.example.demoimagecaching.model.webservice

import com.example.demoimagecaching.model.PhotosModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

const val BASE_URL = "https://api.unsplash.com/photos/"
const val CLIENT_ACCESS_KEY = "tDyJec3eMtiHP_BygNE63h6qo6FoXfGDnJtgn9bX2oc"
const val CLIENT_SECRET_KEY = "YqAQ2wwgyoKO7xivbiaqz4-YrRQ_figySYGDNdakppo"
const val PHOTOS_API = "$BASE_URL?client_id=$CLIENT_ACCESS_KEY"

class WebService {
   private lateinit var apiInterface: APIInterface

   init {
       val retrofit = Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
       apiInterface = retrofit.create(APIInterface::class.java)
   }

    suspend fun getPhotosData() : MutableList<PhotosModel> {
        return apiInterface.getPhotos()
    }

    suspend fun getDownloadPhoto(url: String?) : Call<ResponseBody?> {
        return apiInterface.downloadImageFile(url)
    }

    internal interface APIInterface {
        @GET(PHOTOS_API)
        suspend fun getPhotos(): MutableList<PhotosModel>

        @Headers("Content-Type: application/json")
        @GET
        fun downloadImageFile(@Url url: String?): Call<ResponseBody?>
    }
}