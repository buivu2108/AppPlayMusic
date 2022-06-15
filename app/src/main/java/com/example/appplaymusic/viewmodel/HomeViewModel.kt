package com.example.appplaymusic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appplaymusic.api.Api
import com.example.appplaymusic.base.SingleLiveEvent
import com.example.appplaymusic.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val subscriptions = CompositeDisposable()
    private var dataAdsDisposable: Disposable? = null
    private var playListDisposable: Disposable? = null
    private var listLikeSongDisposable: Disposable? = null
    private var chuDeDisposable: Disposable? = null
    var data = MutableLiveData<DataAds>()
    var dataMusicPlayList = MutableLiveData<DataListMusic>()
    var dataCategory = MutableLiveData<DataCategory>()
    var dataPlayList = MutableLiveData<DataPlayList>()
    var dataChuDe = MutableLiveData<DataChuDe>()
    var dataListLikeSong = MutableLiveData<DataListMusic>()
    private val _actionShowCallApiFail = SingleLiveEvent<Unit>()
    val actionShowCallApiFail: LiveData<Unit>
        get() = _actionShowCallApiFail
    private val _loadSuccessProgressBar = SingleLiveEvent<Unit>()
    val loadSuccessProgressBar: LiveData<Unit>
        get() = _loadSuccessProgressBar

    fun callApi() {
        dataAdsDisposable?.dispose()
        dataAdsDisposable = Api.retrofit.dataAds().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                data.value = it
            }, {
                _actionShowCallApiFail.call()
            }).addTo(subscriptions)

        playListDisposable?.dispose()
        playListDisposable = Api.retrofit.getPlayList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                dataPlayList.value = it
            }, {
                _actionShowCallApiFail.call()
            }).addTo(subscriptions)
        chuDeDisposable?.dispose()
        chuDeDisposable = Api.retrofit.getChuDe().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                dataChuDe.value = it
            }, {
                _actionShowCallApiFail.call()
            }).addTo(subscriptions)
        listLikeSongDisposable?.dispose()
        listLikeSongDisposable = Api.retrofit.getListLikeSong().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                dataListLikeSong.value = it
            }, {
                _actionShowCallApiFail.call()
            }).addTo(subscriptions)
    }

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

    fun topicGetListMusic(idPlayList: String) {
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

    fun getListCategory(idChuDe: String) {
        val retrofitData = Api.retrofit.getListCategory(idChuDe)
        retrofitData.enqueue(object : Callback<DataCategory> {
            override fun onResponse(call: Call<DataCategory>, response: Response<DataCategory>) {
                dataCategory.value = response.body()
                _loadSuccessProgressBar.call()
            }

            override fun onFailure(call: Call<DataCategory>, t: Throwable) {
                _actionShowCallApiFail.call()
            }
        })

    }
}