package com.example.appplaymusic.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appplaymusic.*
import com.example.appplaymusic.adapter.AdsAdapter
import com.example.appplaymusic.adapter.ListLikeSongAdapter
import com.example.appplaymusic.adapter.TopicAdapter
import com.example.appplaymusic.databinding.FragmentHomeBinding
import com.example.appplaymusic.model.DataPlayList
import com.example.appplaymusic.model.DataPlayListItem
import com.example.appplaymusic.viewmodel.HomeViewModel
import me.relex.circleindicator.CircleIndicator3


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var bannerAdapter: AdsAdapter
    lateinit var topicAdapter: TopicAdapter
    lateinit var listLikeSongAdapter: ListLikeSongAdapter
    private lateinit var bannerPage: ViewPager2
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
        initEvent()

        bannerAdapter = AdsAdapter(onItemClick = {
            val listMusic = ListMusicFragment.newInstance(
                it.idQuangCao ?: "1",
                it.hinhAnh,
                it.tenBaiHat,
                it.hinhBaiHat
            )
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_content_id, listMusic)
                .addToBackStack("list_music").commit()
        })
        topicAdapter = TopicAdapter(onItemClick = { dataChuDe, position ->
            homeViewModel.getListCategory(dataChuDe[position].idChuDe.toString())
            binding.loadPlayList.visibility = View.VISIBLE
        })
        binding.rcvTopic.adapter = topicAdapter
        val horizontalLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rcvTopic.layoutManager = horizontalLayoutManager
        bannerPage = binding.viewPageAds
        bannerPage.adapter = bannerAdapter
        val indicator: CircleIndicator3 = binding.indicator
        indicator.setViewPager(bannerPage)
        bannerPage.autoScroll(5000)
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
    }

    private fun initViewModels() {
        homeViewModel.apply {
            callApi()
            data.observe(viewLifecycleOwner) {
                bannerAdapter.addData(it)
            }
            dataChuDe.observe(viewLifecycleOwner) {
                topicAdapter.addData(it)
            }
            dataCategory.observe(viewLifecycleOwner) {
                binding.loadPlayList.visibility = View.GONE
                val seeMoreAllTopicFragment = SeeMoreAllTopicFragment.newInstance(it)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_content_id, seeMoreAllTopicFragment)
                    .addToBackStack("all_topic").commit()
            }
            actionShowCallApiFail.observe(viewLifecycleOwner) {
                Toast.makeText(context, "Call Api Fail", Toast.LENGTH_LONG).show()
            }
            dataPlayList.observe(viewLifecycleOwner) {
                dataPlayList(it)
            }
            dataMusicPlayList.observe(viewLifecycleOwner) {
                val listMusic = PlayMusicFragment.newInstance(it, 0)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_content_id, listMusic)
                    .addToBackStack("play_music").commit()
            }
            dataListLikeSong.observe(viewLifecycleOwner) {
                listLikeSongAdapter.addData(it)
            }
            loadSuccessProgressBar.observe(viewLifecycleOwner) {
                binding.loadPlayList.visibility = View.GONE
            }
        }
    }

    private fun initEvent() {

    }

    private fun dataPlayList(data: DataPlayList) {
        val positionOne = (0 until data.size).toMutableList()
        val shuffleArray = positionOne.shuffled().toIntArray()
        setUpViewPlayList(
            data[shuffleArray[0]],
            binding.imgPlayListOne,
            binding.icPlayListOne,
            binding.namePlayListOne
        )
        setUpViewPlayList(
            data[shuffleArray[1]],
            binding.imgPlayListTwo,
            binding.icPlayListTwo,
            binding.namePlayListTwo
        )
        setUpViewPlayList(
            data[shuffleArray[2]],
            binding.imgPlayListThree,
            binding.icPlayListThree,
            binding.namePlayListThree
        )
        binding.imgPlayListOne.setOnClickListener {
            binding.loadPlayList.visibility = View.VISIBLE
            homeViewModel.callApiPlayList(data[shuffleArray[0]].idPlayList ?: "1")
        }
        binding.imgPlayListTwo.setOnClickListener {
            binding.loadPlayList.visibility = View.VISIBLE
            homeViewModel.callApiPlayList(data[shuffleArray[1]].idPlayList ?: "1")
        }
        binding.imgPlayListThree.setOnClickListener {
            binding.loadPlayList.visibility = View.VISIBLE
            homeViewModel.callApiPlayList(data[shuffleArray[2]].idPlayList ?: "1")
        }
        binding.seeMorePlayList.setOnClickListener {
            val allPlayListFragment = SeeMoreAllPlayListFragment.newInstance(data)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_content_id, allPlayListFragment)
                .addToBackStack("all_play_list").commit()
        }
    }

    private fun setUpViewPlayList(
        data: DataPlayListItem,
        imgPlayListOne: ImageView,
        icPlayListOne: ImageView,
        namePlayListOne: TextView
    ) {
        val imgWaitLoader = RequestOptions().placeholder(R.drawable.bg_lofi_sad).centerCrop()
        Glide.with(this).load(data.hinhNen).apply(imgWaitLoader)
            .into(imgPlayListOne)
        Glide.with(this).load(data.hinhIcon).apply(imgWaitLoader)
            .into(icPlayListOne)
        namePlayListOne.text = data.ten.toString()
    }

    private fun ViewPager2.autoScroll(interval: Long) {
        val handler = Handler()
        var scrollPosition = 0
        val runnable = object : Runnable {
            override fun run() {
                var count = bannerAdapter.itemCount
                if (count == 0) count = 1
                setCurrentItem(scrollPosition++ % count, true)

                handler.postDelayed(this, interval)
            }
        }
        bannerPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                scrollPosition = position + 1
            }
        })
        handler.post(runnable)
    }
}