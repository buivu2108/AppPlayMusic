package com.example.appplaymusic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appplaymusic.*
import com.example.appplaymusic.adapter.ListLikeSongAdapter
import com.example.appplaymusic.databinding.FragmentHomeBinding
import com.example.appplaymusic.model.DataListMusic
import com.example.appplaymusic.model.DataListMusicItem
import com.example.appplaymusic.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var listLikeSongAdapter: ListLikeSongAdapter

    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()

        val mListMusicItem = DataListMusic()
        val dataListMusicItem1 = DataListMusicItem("STAR SEED & Cafe Disko",R.raw.innocent,"Innocent")
        val dataListMusicItem2 = DataListMusicItem("Egzod & Arcando",R.raw.runaway,"Runaway")
        val dataListMusicItem3 = DataListMusicItem("Cartoon",R.raw.onandon,"On & On")
        val dataListMusicItem4 = DataListMusicItem("Tobu",R.raw.ifidisappear,"If I Disappear")
        mListMusicItem.add(dataListMusicItem1)
        mListMusicItem.add(dataListMusicItem2)
        mListMusicItem.add(dataListMusicItem3)
        mListMusicItem.add(dataListMusicItem4)

        listLikeSongAdapter = ListLikeSongAdapter(onItemClick ={ dataListMusic, position ->
            val listMusic = PlayMusicFragment.newInstance(dataListMusic, position )
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_content_id, listMusic)
                .addToBackStack("list_favorite").commit()
            listLikeSongAdapter.changeSelectedSong(position )
        })
        val layoutManager = LinearLayoutManager(context)
        binding.rcvMaybeLike.adapter = listLikeSongAdapter
        binding.rcvMaybeLike.layoutManager = layoutManager
        listLikeSongAdapter.addData(mListMusicItem)
    }

    private fun initViewModels() {
        homeViewModel.apply {
            callApi()
            actionShowCallApiFail.observe(viewLifecycleOwner) {
                Toast.makeText(context, "Call Api Fail", Toast.LENGTH_LONG).show()
            }
        }
    }





}