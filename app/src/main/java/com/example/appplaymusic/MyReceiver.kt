package com.example.appplaymusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val actionMusic = intent?.getIntExtra("action_music", 0)
        val intentService = Intent(context,MusicService::class.java)
        intentService.putExtra("action_music_service",actionMusic)
        context?.startService(intentService)
    }
}