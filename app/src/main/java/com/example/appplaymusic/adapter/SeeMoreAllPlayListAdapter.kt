package com.example.appplaymusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appplaymusic.R
import com.example.appplaymusic.databinding.ItemSeeMoreAllBinding
import com.example.appplaymusic.model.DataChuDe
import com.example.appplaymusic.model.DataPlayList

class SeeMoreAllPlayListAdapter(val onItemClick: (DataPlayList, Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    private var mListPlayList = DataPlayList()


    @SuppressLint("NotifyDataSetChanged")
    fun addData(data: DataPlayList) {
        mListPlayList.clear()
        mListPlayList.addAll(data)
        notifyDataSetChanged()

    }

    private inner class VH(val binding: ItemSeeMoreAllBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun fillData(dataPlayList: DataPlayList, position: Int) {
            binding.namePlayList.text = dataPlayList[position].ten.toString()
            val imgWaitLoader = RequestOptions().placeholder(R.drawable.bg_lofi_sad).centerCrop()
            Glide.with(context).load(dataPlayList[position].hinhIcon).apply(imgWaitLoader)
                .into(binding.icPlayList)
            binding.bgPlayList.setOnClickListener {
                onItemClick.invoke(dataPlayList, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val binding = ItemSeeMoreAllBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VH).fillData(mListPlayList, position)
    }

    override fun getItemCount(): Int {
        return mListPlayList.size
    }
}