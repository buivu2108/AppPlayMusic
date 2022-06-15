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

class SeeMoreAllTopicViewModel : ViewModel() {
    var dataMusicPlayList = MutableLiveData<DataListMusic>()
    private val _actionShowCallApiFail = SingleLiveEvent<Unit>()
    val actionShowCallApiFail: LiveData<Unit>
        get() = _actionShowCallApiFail
    private val _loadSuccessProgressBar = SingleLiveEvent<Unit>()
    val loadSuccessProgressBar: LiveData<Unit>
        get() = _loadSuccessProgressBar

    fun getListMusicInCategory(idCategory: String) {
        val retrofitData = Api.retrofit.getListMusicInCategory(idCategory)
        retrofitData.enqueue(object : Callback<DataListMusic> {
            override fun onResponse(call: Call<DataListMusic>, response: Response<DataListMusic>) {
                dataMusicPlayList.value = response.body()
                _loadSuccessProgressBar.call()
            }
            override fun onFailure(call: Call<DataListMusic>, t: Throwable) {
                _actionShowCallApiFail.call()
            }
        })
    }
}