package com.example.appplaymusic.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataListMusicItem(
    @SerializedName("CaSi")
    val caSi: String,
    @SerializedName("LinkBaiHat")
    val linkBaiHat: Int,
    @SerializedName("TenBaiHat")
    val tenBaiHat: String
) : Serializable