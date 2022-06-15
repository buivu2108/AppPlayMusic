package com.example.appplaymusic.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    private val gson = GsonBuilder()
        .setDateFormat("EEEE yyyy-MM-dd HH:mm:ss")
        .create()
    val retrofit: Service = Retrofit.Builder()
        .baseUrl("https://adsadasad.000webhostapp.com/Sever/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(Service::class.java)

}