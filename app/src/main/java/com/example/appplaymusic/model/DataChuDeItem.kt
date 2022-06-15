package com.example.appplaymusic.model


import com.google.gson.annotations.SerializedName

data class DataChuDeItem(
    @SerializedName("HinhChuDe")
    val hinhChuDe: String?,
    @SerializedName("IdChuDe")
    val idChuDe: String?,
    @SerializedName("TenChuDe")
    val tenChuDe: String?
)