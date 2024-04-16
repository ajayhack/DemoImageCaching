package com.example.demoimagecaching.model

import androidx.compose.runtime.Immutable

@Immutable
data class PhotosModel(var id : String? = null , var title : String? = null , var coverageURL : String? = null , var backupDetails : BackUpDetails? = null)

@Immutable
data class BackUpDetails(var pdfLink : String? = null ,
                      var screenshotURL : String? = null)