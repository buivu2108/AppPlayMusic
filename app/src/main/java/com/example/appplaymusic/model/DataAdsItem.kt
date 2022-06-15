package com.example.appplaymusic.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataAdsItem(
    @SerializedName("HinhAnh")
    val hinhAnh: String?,
    @SerializedName("HinhBaiHat")
    val hinhBaiHat: String?,
    @SerializedName("IdBaiHat")
    val idBaiHat: String?,
    @SerializedName("IdQuangCao")
    val idQuangCao: String?,
    @SerializedName("NoiDung")
    val noiDung: String?,
    @SerializedName("TenBaiHat")
    val tenBaiHat: String?
) : Serializable