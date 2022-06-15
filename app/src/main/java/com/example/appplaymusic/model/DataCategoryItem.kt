package com.example.appplaymusic.model


import com.google.gson.annotations.SerializedName

data class DataCategoryItem(
    @SerializedName("HinhTheLoai")
    val hinhTheLoai: String?,
    @SerializedName("IdTheLoai")
    val idTheLoai: String?,
    @SerializedName("TenTheLoai")
    val tenTheLoai: String?
)