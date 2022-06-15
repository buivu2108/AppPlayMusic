package com.example.appplaymusic.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataListMusicItem(
    @SerializedName("CaSi")
    val caSi: String?,
    @SerializedName("HinhBaiHat")
    val hinhBaiHat: String?,
    @SerializedName("IdBaiHat")
    val idBaiHat: String?,
    @SerializedName("LinkBaiHat")
    val linkBaiHat: String?,
    @SerializedName("LuotThich")
    val luotThich: String?,
    @SerializedName("TenBaiHat")
    val tenBaiHat: String?
) : Serializable