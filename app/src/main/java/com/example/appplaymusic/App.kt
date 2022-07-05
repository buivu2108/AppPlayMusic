package com.example.appplaymusic

import android.app.Application
import com.google.gson.Gson

class App : Application() {
    var gSon: Gson? = null
    override fun onCreate() {
        super.onCreate()
        application = this
        gSon = Gson()

    }

    companion object {
        private lateinit var application: App
        fun get (): App {
            return application
        }
    }
}