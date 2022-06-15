package com.example.appplaymusic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appplaymusic.api.Api
import com.example.appplaymusic.base.SingleLiveEvent
import com.example.appplaymusic.model.DataAds
import com.example.appplaymusic.model.DataListMusic
import com.example.appplaymusic.model.DataPlayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeeMoreAllPlayListViewModel : ViewModel() {
    var dataMusicPlayList = MutableLiveData<DataListMusic>()
    private val _actionShowCallApiFail = SingleLiveEvent<Unit>()
    val actionShowCallApiFail: LiveData<Unit>
        get() = _actionShowCallApiFail
    private val _loadSuccessProgressBar = SingleLiveEvent<Unit>()
    val loadSuccessProgressBar: LiveData<Unit>
        get() = _loadSuccessProgressBar

    fun callApiPlayList(idPlayList: String) {
        val retrofitData = Api.retrofit.getListMusicPlayList(idPlayList)
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