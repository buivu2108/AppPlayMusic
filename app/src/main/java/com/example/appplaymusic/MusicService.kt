package com.example.appplaymusic

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.appplaymusic.model.DataListMusic
import com.example.appplaymusic.model.DataListMusicItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MusicService : Service() {
    private var player: ExoPlayer? = null
    lateinit var songList: DataListMusic
    private var position: Int = 0
    private var ACTION_PAUSE: Int = 1
    private var ACTION_PlAY: Int = 2
    private var ACTION_NEXT: Int = 3
    private var ACTION_PREVIOUS: Int = 4
    private var ACTION_CHOOSE: Int = 5
    private var ACTION_SYNC_MUSIC: Int = 7
    private var ACTION_RANDOM_MUSIC: Int = 8
    private var autoRefreshDisposable: Disposable? = null
    private var seekBarSongProgress: Int = 0
    private var ACTION_SEEK_MUSIC: Int = 6
    private var isSyncMusic: Boolean = false
    private var isRandomMusic: Boolean = false
    private lateinit var dateFormat: DateFormat


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this)
            .build()
            .also {
                it.playWhenReady = true
                it.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            changeSong(position + 1)
                        } else if (playbackState == Player.STATE_READY) {
                            setTimeTotal()
                            updateTime()
                            if (player?.isPlaying == false) {
                                createExoPlayerMusic()
                            }
                        }
                    }
                })
            }

    }

    private fun setTimeTotal() {
        @SuppressLint("SimpleDateFormat")
        dateFormat = SimpleDateFormat("mm:ss")
        val timeKt: String = dateFormat.format(player?.duration ?: 0)
        seekBarSongProgress = (player?.duration?.toInt() ?: 0) / 1000
        val intent = Intent("setTimeTotal")
        intent.putExtra("timeKt", timeKt)
        intent.putExtra("seekBarSongProgress", seekBarSongProgress)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun updateTime() {
        autoRefreshDisposable?.dispose()
        autoRefreshDisposable =
            Observable.interval(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateView()
                }, {
                })
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateView() {
        val timeUpdate: String = dateFormat.format(player?.currentPosition ?: 0)
        seekBarSongProgress = (player?.currentPosition?.toInt() ?: 0) / 1000
        val intent = Intent("upDateTime")
        intent.putExtra("timeUpdate", timeUpdate)
        intent.putExtra("seekBarProgressUpdate", seekBarSongProgress)
        intent.putExtra("isPlayingMusic", player?.isPlaying == true)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.getSerializable("dataMusicListSong") != null) {
                val dataMusicListSong =
                    bundle.getSerializable("dataMusicListSong") as ArrayList<DataListMusicItem>
                songList = DataListMusic()
                songList.clear()
                songList.addAll(dataMusicListSong)
                position = bundle.getInt("positionMusic")
                createExoPlayerMusic()
                startForegroundService(songList, position)
            }
        }
        val actionMusic: Int = intent.getIntExtra("action_music_service", 0)
        val positionMusicSong: Int = intent.getIntExtra("positionSongMusic", 0)
        handleActionMusic(actionMusic, positionMusicSong)
        return START_NOT_STICKY
    }

    private fun changeSong(positionNum: Int) {
        if (isSyncMusic) {
            createExoPlayerMusic()
            startForegroundService(songList, position)
        } else if (isRandomMusic) {
            val randomSong = Random.nextInt(0, songList.size)
            position = randomSong
            createExoPlayerMusic()
            startForegroundService(songList, position)
        } else {
            position = when {
                positionNum > songList.size - 1 -> {
                    0
                }
                positionNum < 0 -> {
                    songList.size - 1
                }
                else -> {
                    positionNum
                }
            }
            if (player?.isPlaying == true) {
                player?.stop()
            }
            createExoPlayerMusic()
            startForegroundService(songList, position)
        }

    }

    private fun createExoPlayerMusic() {
        val url = songList[position].linkBaiHat.toString()
        player?.setMediaItem(MediaItem.fromUri(url))
        player?.prepare()
        player?.play()
    }

    private fun handleActionMusic(action: Int, positionMusicSong: Int) {
        when (action) {
            ACTION_PAUSE -> {
                pauseMusic()
            }
            ACTION_PlAY -> {
                playMusic()
            }
            ACTION_PREVIOUS -> {
                previousMusic()
            }
            ACTION_NEXT -> {
                nextMusic()
            }
            ACTION_CHOOSE -> {
                chooseMusic(positionMusicSong)
            }
            ACTION_SEEK_MUSIC -> {
                seekMusic(positionMusicSong)
            }
            ACTION_SYNC_MUSIC -> {
                syncMusic()
            }
        }

    }

    private fun syncMusic() {
        isSyncMusic = !isSyncMusic
    }

    private fun seekMusic(seekBarProgress: Int) {
        if (player != null) {
            player?.seekTo(seekBarProgress.toLong())
        }
    }

    private fun chooseMusic(songChoose: Int) {
        if (player != null) {
            changeSong(songChoose)
        }
    }

    private fun nextMusic() {
        if (player != null) {
            changeSong(position + 1)
        }
    }

    private fun previousMusic() {
        if (player != null) {
            changeSong(position - 1)
        }
    }

    private fun playMusic() {
        if (player != null && player?.isPlaying == false) {
            player?.play()
            val intent = Intent("playMusic")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    private fun pauseMusic() {
        if (player != null && player?.isPlaying == true) {
            player?.pause()
            val intent = Intent("pauseMusic")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    private fun startForegroundService(listMusic: DataListMusic, position: Int) {
//        val intent = Intent(this@MusicService, MainActivity::class.java)
//
//        @SuppressLint("UnspecifiedImmutableFlag")
//        val pendingIntent = PendingIntent.getActivity(
//            this@MusicService,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//       @SuppressLint("RemoteViewLayout")
//        val remoteViews = RemoteViews(packageName, R.layout.custom_notification)
//        remoteViews.setTextViewText(R.id.nameSinger, listMusic[position].caSi)
//        remoteViews.setTextViewText(R.id.nameMusic, listMusic[position].tenBaiHat)
        Glide.with(this)
            .asBitmap()
            .load(listMusic[position].hinhBaiHat)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {
                        sendNotification(resource)
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun sendNotification(bitmap: Bitmap) {
        //      remoteViews.setImageViewBitmap(R.id.imgMusicNotification, bitmap)
//        remoteViews.setImageViewResource(R.id.btnPreviousNotification, R.drawable.ic_back)
//        remoteViews.setImageViewResource(R.id.btnStopNotification, R.drawable.ic_pause)
//        remoteViews.setImageViewResource(R.id.btnNextNotification, R.drawable.ic_next)
//
//        if (player?.isPlaying == true) {
//            remoteViews.setOnClickPendingIntent(
//                R.id.btnStopNotification,
//                getPendingIntent(this@MusicService, ACTION_PAUSE)
//            )
//            remoteViews.setImageViewResource(R.id.btnStopNotification, R.drawable.ic_pause)
//        } else {
//            remoteViews.setOnClickPendingIntent(
//                R.id.btnStopNotification,
//                getPendingIntent(this@MusicService, ACTION_PlAY)
//            )
//            remoteViews.setImageViewResource(R.id.btnStopNotification, R.drawable.ic_play)
//        }
//        remoteViews.setOnClickPendingIntent(
//            R.id.btnPreviousNotification,
//            getPendingIntent(this@MusicService, ACTION_PREVIOUS)
//        )
//        remoteViews.setOnClickPendingIntent(
//            R.id.btnNextNotification,
//            getPendingIntent(this@MusicService, ACTION_NEXT)
//        )

        val mediaSession = MediaSessionCompat(this, "tag")
        val notification = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID3)
            .setSmallIcon(R.drawable.ic_smal_music)
            .addAction(
                R.drawable.ic_back,
                "Previous",
                getPendingIntent(this@MusicService, ACTION_PREVIOUS)
            )
            .addAction(
                R.drawable.ic_pause, "Pause", getPendingIntent(this@MusicService, ACTION_PAUSE)
            )
            .addAction(
                R.drawable.ic_play, "Play", getPendingIntent(this@MusicService, ACTION_PlAY)
            )
            .addAction(R.drawable.ic_next, "Next", getPendingIntent(this@MusicService, ACTION_NEXT))
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0)
                    .setShowActionsInCompactView(1)
                    .setShowActionsInCompactView(2)
                    .setShowActionsInCompactView(3)
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setContentTitle(songList[position].tenBaiHat)
            .setContentText(songList[position].caSi)
            .setLargeIcon(bitmap)
            .build()
        startForeground(1, notification)
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent? {
        val intent = Intent(this, MyReceiver::class.java)
        intent.putExtra("action_music", action)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (player != null) {
            player?.release()
            player = null
        }
    }
}