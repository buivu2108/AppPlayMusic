package com.example.appplaymusic.api

import com.example.appplaymusic.model.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface Service {

    @GET("songbanner.php")
    fun dataAds(): Single<DataAds>

    @GET("playlist.php")
    fun getPlayList(): Single<DataPlayList>

    @GET("getlisttopic.php")
    fun getChuDe(): Single<DataChuDe>

    @GET("getlistlikesong.php")
    fun getListLikeSong(): Single<DataListMusic>

    @FormUrlEncoded
    @POST("listmusic.php")
    fun getListMusic(@Field("idquangcao") idAds: String?): Call<DataListMusic>

    @FormUrlEncoded
    @POST("playlistmusic.php")
    fun getListMusicPlayList(@Field("idquangcao") idPlayList: String?): Call<DataListMusic>

    @FormUrlEncoded
    @POST("getlistcategori.php")
    fun getListCategory(@Field("idchude") idChuDe: String?): Call<DataCategory>

    @FormUrlEncoded
    @POST("categorymusic.php")
    fun getListMusicInCategory(@Field("idcategory") idCategory: String?): Call<DataListMusic>

    @FormUrlEncoded
    @POST("searchsong.php")
    fun searchListMusic(@Field("tukhoa") tukhoa: String?): Call<DataListMusic>
}