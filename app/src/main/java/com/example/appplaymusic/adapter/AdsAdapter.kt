package com.example.appplaymusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appplaymusic.R
import com.example.appplaymusic.databinding.ItemAdsBinding
import com.example.appplaymusic.model.DataAds
import com.example.appplaymusic.model.DataAdsItem

class AdsAdapter(val onItemClick: (DataAdsItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    private val arrayListAds = ArrayList<DataAdsItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun addData(dataAds: DataAds) {
        arrayListAds.clear()
        arrayListAds.addAll(dataAds)
        notifyDataSetChanged()
    }

    private inner class VH(val binding: ItemAdsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun fillData(dataAdsItem: DataAdsItem) {
            val imgWaitLoader = RequestOptions().placeholder(R.drawable.ic_vo_ham).centerCrop()
            Glide.with(context).load(dataAdsItem.hinhAnh).apply(imgWaitLoader)
                .into(binding.imgAds)
            Glide.with(context).load(dataAdsItem.hinhBaiHat).apply(imgWaitLoader)
                .into(binding.imgSong)
            binding.tvNameSong.text = dataAdsItem.tenBaiHat.toString()
            binding.tvContentSong.text = dataAdsItem.noiDung.toString()
            binding.bgAds.setOnClickListener {
                onItemClick.invoke(dataAdsItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val binding = ItemAdsBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val dataAdsItem = arrayListAds[position]
        (holder as VH).fillData(dataAdsItem)
    }

    override fun getItemCount(): Int {
        return arrayListAds.size
    }
}

