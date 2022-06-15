package com.example.appplaymusic.base

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable

abstract class BaseRequest(@SerializedName("api")
                       var api: String? = "",
                       @SerializedName("token") open var token: String? = null) : Serializable {

    override fun toString(): String {
        return Gson().toJson(this)
    }
}