package com.example.appplaymusic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appplaymusic.api.Api
import com.example.appplaymusic.base.SingleLiveEvent
import com.example.appplaymusic.model.DataListMusic
import com.hadilq.liveevent.LiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class PlayMusicViewModel : ViewModel() {
    private var thisPosition: Int? = null
    private val _isPlaying = MutableLiveData<Boolean>()
    private val _isSyncMusic = MutableLiveData(false)
    private val _isRandomMusic = MutableLiveData(false)

    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    val isSyncMusic: LiveData<Boolean>
        get() = _isSyncMusic

    val isRandomMusic: LiveData<Boolean>
        get() = _isRandomMusic

    private val _actionShowCallApiFail = LiveEvent<Unit>()
    val actionShowCallApiFail: LiveData<Unit>
        get() = _actionShowCallApiFail

    private val _positionSong = LiveEvent<Int>()
    val positionSong: MutableLiveData<Int>
        get() = _positionSong

    fun stopPlay() {
        _isPlaying.value = false
    }

    fun startPlay() {
        _isPlaying.value = true
    }

    fun nextSong(position: Int, songListSize: Int) {
        if (_isSyncMusic.value == true) {
            _positionSong.value = position
        } else if (_isRandomMusic.value == true) {
            val randomSong = Random.nextInt(0, songListSize)
            _positionSong.value = randomSong
        } else {
            thisPosition = position + 1
            if ((thisPosition ?: 0) > songListSize - 1) {
                thisPosition = 0
            }
            _positionSong.value = thisPosition ?: 0
        }
    }

    fun previousSong(position: Int, songListSize: Int) {
        if (_isSyncMusic.value == true) {
            _positionSong.value = position
        } else if (_isRandomMusic.value == true) {
            val randomSong = Random.nextInt(0, songListSize)
            _positionSong.value = randomSong
        } else {
            thisPosition = position - 1
            if ((thisPosition ?: 0) < 0) {
                thisPosition = songListSize - 1
            }
            _positionSong.value = thisPosition ?: 0
        }
    }

    fun syncMusic() {
        _isSyncMusic.value = !_isSyncMusic.value!!
    }

    fun randomMusic() {
        _isRandomMusic.value = !_isRandomMusic.value!!
    }

    fun changeSong(positionChange: Int) {
        _positionSong.value = positionChange
    }
}