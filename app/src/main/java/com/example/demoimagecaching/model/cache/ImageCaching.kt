package com.example.demoimagecaching.model.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.demoimagecaching.model.webservice.WebService
import com.example.demoimagecaching.view.MyApplication
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

const val folderName = "unsplashPhotos"

object ImageCaching {
     @JvmStatic
     var imageList : MutableList<String> = mutableListOf()
     private var webService: WebService? = null
    init {
       webService = WebService()
    }

    suspend fun getBitmap(imageId : String? = null , url: String? = null): Bitmap? {
        var bitmap: Bitmap? = null
        val request: Call<ResponseBody?>? = webService?.getDownloadPhoto(url)
        val responseBody = request?.execute()?.body()
        if (responseBody != null) {
            bitmap = getScaledDownBitmapFromUri(responseBody.byteStream())
            if (bitmap != null) {
                val fileName = url.hashCode().toString() + imageId + ".jpg"
                if(!imageList.contains(fileName)){
                    imageList.add(fileName)
                }
                storeImageInLocalCache(bitmap , fileName , getImageDirectory())
            }
            println("BitmapImageList:- $imageList")
        }
        return bitmap
    }

    private fun getFolderOnExternalDirectory(): File? {
        var doesFolderExists = false
        var folderFileObject: File? = null
        val context: Context? = MyApplication.getAppContext()

        val externalFilesDirectory = context?.filesDir
        if (containsData(folderName)) {
            folderFileObject = File(externalFilesDirectory, folderName ?: "")
            doesFolderExists = folderFileObject.exists()

            if (!doesFolderExists) {
                doesFolderExists = folderFileObject.mkdirs()
                if (!doesFolderExists) {
                    folderFileObject = externalFilesDirectory
                }
            }
        } else {
            folderFileObject = externalFilesDirectory
        }
        return folderFileObject
    }

    private fun containsData(data: String?): Boolean {
        var containsData = true
        if (data == null || data.trim { it <= ' ' } == "") {
            containsData = false
        }
        return containsData
    }

    private fun getScaledDownBitmapFromUri(inputStream: InputStream?): Bitmap? {
        var options: BitmapFactory.Options = getBitmapOptions()
        val width = options.outWidth
        val height = options.outHeight
        options = getBitmapOptions(height, width)
        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
        val outStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        return bitmap
    }

    private fun getBitmapOptions(height: Int, width: Int): BitmapFactory.Options {
        var actualRequiredHeight = 0
        var actualRequiredWidth = 0
        if (height < width) {
            actualRequiredHeight = 768
            actualRequiredWidth = 1024
        } else {
            actualRequiredHeight = 1024
            actualRequiredWidth = 768
        }
        val options = BitmapFactory.Options()
        var inSampleSize = 1

        while (height / inSampleSize > actualRequiredHeight || width / inSampleSize > actualRequiredWidth) {
            inSampleSize *= 2
        }
        options.inSampleSize = inSampleSize
        options.inJustDecodeBounds = false
        return options
    }

    private fun getBitmapOptions(): BitmapFactory.Options {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        return options
    }

    private fun getImageDirectory(): String {
        val directory: File? = getFolderOnExternalDirectory()
        return directory?.absolutePath ?: ""
    }

    private fun storeImageInLocalCache(image: Bitmap?, fileName: String, filePath: String): File? {
        var fileObject: File? = null
        run { fileObject = File(filePath + File.separator + fileName) }
        if (image == null) {
            return null
        }
        try {
            if (fileObject != null) {
                val fos = FileOutputStream(fileObject)
                println("ImageFileName:- $fileName")
                image.compress(Bitmap.CompressFormat.PNG, 90, fos)
                fos.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return fileObject
    }

     fun readImageFromDiskCache(fileName: String?) : Bitmap? {
        val storedPreferenceImageList = LocalCachePreference.getFormattedCacheImageListFromPreference()
        if(storedPreferenceImageList.isNullOrEmpty()) return null
         val file = File(getImageDirectory())
         try {
             if(file != null){
                 val imageFile = File(file.absolutePath + File.separator + fileName)
                 val streamIn = FileInputStream(imageFile)
                 val bitmap = BitmapFactory.decodeStream(streamIn)
                 streamIn.close()
                 return bitmap
             }
         }catch (ex : Exception){
             ex.printStackTrace()
         }
         return null
    }
}