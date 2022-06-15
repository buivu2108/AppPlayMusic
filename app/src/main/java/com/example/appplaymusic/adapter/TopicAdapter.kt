package com.example.appplaymusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appplaymusic.R
import com.example.appplaymusic.databinding.ItemTopicBinding
import com.example.appplaymusic.model.DataChuDe
import com.example.appplaymusic.model.DataChuDeItem

class TopicAdapter(val onItemClick: (DataChuDe,Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    private val arrayListChuDe = DataChuDe()

    @SuppressLint("NotifyDataSetChanged")
    fun addData(dataAds: DataChuDe) {
        arrayListChuDe.clear()
        arrayListChuDe.addAll(dataAds)
        notifyDataSetChanged()
    }

    private inner class VH(val binding: ItemTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun fillData(dataChuDe: DataChuDe, position: Int) {
            val imgWaitLoader = RequestOptions().placeholder(R.drawable.ic_vo_ham).centerCrop()
            Glide.with(context).load(dataChuDe[position].hinhChuDe).apply(imgWaitLoader)
                .into(binding.imgTopic)
            binding.bgTopic.setOnClickListener {
                onItemClick.invoke(dataChuDe,position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val binding = ItemTopicBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VH).fillData(arrayListChuDe,position)
    }

    override fun getItemCount(): Int {
        return arrayListChuDe.size
    }
}

