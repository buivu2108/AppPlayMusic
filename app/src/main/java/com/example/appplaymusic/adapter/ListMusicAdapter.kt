package com.example.appplaymusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appplaymusic.databinding.ItemListMusicBinding
import com.example.appplaymusic.model.DataListMusic

class ListMusicAdapter(val onItemClick: (DataListMusic, Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    private var mListMusic = DataListMusic()

    @SuppressLint("NotifyDataSetChanged")
    fun addData(data: DataListMusic) {
        mListMusic.clear()
        mListMusic.addAll(data)
        notifyDataSetChanged()

    }

    private inner class VH(val binding: ItemListMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun fillData(listSong: DataListMusic, position: Int) {
            binding.nameMusic.text = listSong[position].tenBaiHat.toString()
            binding.numMusic.text = (position+1).toString()
            binding.nameSinger.text = listSong[position].caSi.toString()

            binding.bgSong.setOnClickListener {
                onItemClick.invoke(listSong,position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val binding = ItemListMusicBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VH).fillData(mListMusic, position)
    }

    override fun getItemCount(): Int {
        return mListMusic.size
    }


}