package com.example.appplaymusic.model


import com.google.gson.annotations.SerializedName

data class DataPlayListItem(
    @SerializedName("HinhIcon")
    val hinhIcon: String?,
    @SerializedName("HinhNen")
    val hinhNen: String?,
    @SerializedName("IdPlayList")
    val idPlayList: String?,
    @SerializedName("Ten")
    val ten: String?
)