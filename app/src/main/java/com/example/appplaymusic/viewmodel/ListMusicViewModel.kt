package com.example.appplaymusic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appplaymusic.api.Api
import com.example.appplaymusic.base.SingleLiveEvent
import com.example.appplaymusic.model.DataListMusic
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMusicViewModel : ViewModel() {
    private val subscriptions = CompositeDisposable()
    private var dataListMusicDisposable: Disposable? = null
    var data = MutableLiveData<DataListMusic>()
    private val _actionShowCallApiFail = SingleLiveEvent<Unit>()
    val actionShowCallApiFail: LiveData<Unit>
        get() = _actionShowCallApiFail

    fun callApi(idAds: String) {
        dataListMusicDisposable?.dispose()
        val retrofitData = Api.retrofit.getListMusic(idAds)
        retrofitData.enqueue(object : Callback<DataListMusic> {
            override fun onResponse(call: Call<DataListMusic>, response: Response<DataListMusic>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<DataListMusic>, t: Throwable) {
                _actionShowCallApiFail.call()
            }
        })
    }
}