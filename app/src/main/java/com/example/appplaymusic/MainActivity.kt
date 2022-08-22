package com.example.appplaymusic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.appplaymusic.adapter.MainViewPagerAdapter
import com.example.appplaymusic.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewPagerMusic: ViewPager2
    lateinit var tabMusic: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

    }
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
                    tab.text = "HOME"
                }
                1 -> {
                    tab.icon = getDrawable(R.drawable.hearticon)
                    tab.text = "PURCHASE"
                }
            }
        }.attach()
    }
}