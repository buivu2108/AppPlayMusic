package com.example.appplaymusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appplaymusic.R
import com.example.appplaymusic.databinding.ItemSongBinding
import com.example.appplaymusic.model.DataListMusic

class RcvSongAdapter(val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    private var mListMusic = DataListMusic()
    private var selectedSong: Int = 0

    @SuppressLint("NotifyDataSetChanged")
    fun addData(data: DataListMusic) {
        mListMusic.clear()
        mListMusic.addAll(data)
        notifyDataSetChanged()

    }

    fun changeSelectedSong(songSelected: Int) {
        selectedSong = when {
            songSelected > mListMusic.size - 1 -> { //nếu songList có 6 bài position quay về 0
                0
            }
            songSelected < 0 -> {
                mListMusic.size - 1
            }
            else -> {
                songSelected
            }
        }
        notifyDataSetChanged()
    }

    private inner class VH(val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun fillData(listSong: DataListMusic, position: Int) {
            binding.nameMusic.text = listSong[position].tenBaiHat.toString()
            binding.numMusic.text = (position + 1).toString()
            binding.nameSinger.text = listSong[position].caSi.toString()
            binding.songFavorite.visibility = View.GONE
            binding.bgSong.setOnClickListener {
                onItemClick.invoke(position)
            }
            if (selectedSong == position) {
                binding.bgSong.setBackgroundResource(R.drawable.custom_back_ground)
            }else{
                binding.bgSong.setBackgroundResource(R.drawable.back_ground)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val binding = ItemSongBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VH).fillData(mListMusic, position)
    }

    override fun getItemCount(): Int {
        return mListMusic.size
    }


}