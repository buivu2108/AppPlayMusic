package com.example.appplaymusic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appplaymusic.api.Api
import com.example.appplaymusic.base.SingleLiveEvent
import com.example.appplaymusic.model.DataListMusic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    var dataListSearchSong = MutableLiveData<DataListMusic>()
    private val _actionShowCallApiFail = SingleLiveEvent<Unit>()
    val actionShowCallApiFail: LiveData<Unit>
        get() = _actionShowCallApiFail
    private val _loadSuccessProgressBar = SingleLiveEvent<Unit>()
    val loadSuccessProgressBar: LiveData<Unit>
        get() = _loadSuccessProgressBar

    fun callApi(nameSong:String) {
        val retrofitData = Api.retrofit.searchListMusic(nameSong)
        retrofitData.enqueue(object : Callback<DataListMusic> {
            override fun onResponse(call: Call<DataListMusic>, response: Response<DataListMusic>) {
                dataListSearchSong.value = response.body()
                _loadSuccessProgressBar.call()
            }
            override fun onFailure(call: Call<DataListMusic>, t: Throwable) {
                _actionShowCallApiFail.call()
            }
        })
    }
}