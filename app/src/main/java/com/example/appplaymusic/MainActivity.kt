package com.example.appplaymusic

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.appplaymusic.adapter.MainViewPagerAdapter
import com.example.appplaymusic.databinding.ActivityMainBinding
import com.example.appplaymusic.model.DataListMusicItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewPagerMusic: ViewPager2
    lateinit var tabMusic: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initEven()
    }

    private fun initEven() {

    }

    fun btnStartService(data: DataListMusicItem) {
        val intent = Intent(this, MusicService::class.java)
        val bundle = Bundle()
        bundle.putSerializable("dataListMusicItem", data)
        intent.putExtras(bundle)
        startService(intent)
    }

    fun sendNotification() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_miss)
        val notification = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setContentTitle("Vợ nhớ ck rồi phải không?")
            .setContentText("Nhớ thì mau nên với ck đi nhé")
            .setSmallIcon(R.drawable.hearticon)
            .setLargeIcon(bitmap)
            .setColor(resources.getColor(R.color.design_default_color_error))
            .build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    private val notificationId: Int
        get() = Date().time.toInt()

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {
        viewPagerMusic = binding.viewPagerMusic
        tabMusic = binding.tabMusic
        viewPagerMusic.offscreenPageLimit = 2
        viewPagerMusic.isUserInputEnabled = false
        val mainViewPagerAdapter = MainViewPagerAdapter(this)
        viewPagerMusic.adapter = mainViewPagerAdapter
        TabLayoutMediator(tabMusic, viewPagerMusic) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = getDrawable(R.drawable.hearticon)
                    tab.text = "Trang Chủ"
                }
                1 -> {
                    tab.icon = getDrawable(android.R.drawable.ic_menu_search)
                    tab.text = "Tìm Kiếm"
                }
            }
        }.attach()
    }
}