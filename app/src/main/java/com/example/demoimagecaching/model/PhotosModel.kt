package com.example.demoimagecaching.model

data class PhotosModel(var id : String? = null , var alt_description : String? = null , var urls : URLSData? = null)

data class URLSData(var raw : String? = null ,
                      var full : String? = null ,
                      var regular : String? = null ,
                      var small : String? = null ,
                      var thumb : String? = null,
                      var small_s3 : String? = null)