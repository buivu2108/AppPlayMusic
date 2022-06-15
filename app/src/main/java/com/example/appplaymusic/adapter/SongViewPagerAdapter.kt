package com.example.appplaymusic.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appplaymusic.RcvSongFragment
import com.example.appplaymusic.RoundDiscFragment
import com.example.appplaymusic.model.DataListMusic


class SongViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    private var mSongList = DataListMusic()
    private var mPosition: Int = 0
    @SuppressLint("NotifyDataSetChanged")
    fun addSongList(songList: DataListMusic, position: Int) {
        mSongList.clear()
        mSongList.addAll(songList)
        notifyDataSetChanged()
        mPosition = position
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                RcvSongFragment.newInstance(mSongList,mPosition)
            }
            else -> {
                RoundDiscFragment.newInstance(mSongList,mPosition)
            }
        }
    }

}