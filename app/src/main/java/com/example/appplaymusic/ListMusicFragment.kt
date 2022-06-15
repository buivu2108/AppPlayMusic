package com.example.appplaymusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appplaymusic.adapter.ListMusicAdapter
import com.example.appplaymusic.databinding.FragmentListMusicBinding
import com.example.appplaymusic.viewmodel.ListMusicViewModel


class ListMusicFragment : Fragment() {
    lateinit var binding: FragmentListMusicBinding
    private var idAds: String? = null
    private var imgAds: String? = null
    private val listMusicViewModel: ListMusicViewModel by viewModels()
    lateinit var adapterListMusic: ListMusicAdapter

    companion object {
        fun newInstance(idAds: String, imgAds: String?, nameAds: String?, icAds: String?): ListMusicFragment {
            val args = Bundle()
            args.putString("idAds", idAds)
            args.putString("imgAds", imgAds)
            args.putString("nameAds", nameAds)
            args.putString("icAds", icAds)
            val fragment = ListMusicFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListMusicBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
       adapterListMusic = ListMusicAdapter(onItemClick = { dataListMusic,position ->
           val listMusic = PlayMusicFragment.newInstance(dataListMusic,position)
           requireActivity().supportFragmentManager.beginTransaction()
               .replace(R.id.activity_main_content_id, listMusic)
               .addToBackStack("play_music").commit()
       })
        binding.rcvListMusic.adapter = adapterListMusic
        val layoutManager = LinearLayoutManager(context)
        binding.rcvListMusic.layoutManager = layoutManager
        listMusicViewModel.callApi(idAds ?: "1")
        listMusicViewModel.data.observe(viewLifecycleOwner) {
            adapterListMusic.addData(it)
        }
        listMusicViewModel.actionShowCallApiFail.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Call Api Fail", Toast.LENGTH_LONG).show()
        }
    }

    private fun initEvent() {
        binding.btnBackHome.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.bgListMusic.setOnClickListener { return@setOnClickListener }
        binding.icListMusic.setOnClickListener { return@setOnClickListener }
        binding.nameSong.setOnClickListener { return@setOnClickListener }
    }

    private fun initView() {
        idAds = requireArguments().getString("idAds")
        imgAds = requireArguments().getString("imgAds")
        val nameAds = requireArguments().getString("nameAds")
        val icAds = requireArguments().getString("icAds")
        val imgWaitLoader = RequestOptions().placeholder(R.drawable.bg_lofi_sad).centerCrop()
        Glide.with(this).load(imgAds).apply(imgWaitLoader)
            .into(binding.bgListMusic)
        Glide.with(this).load(icAds).apply(imgWaitLoader)
            .into(binding.icListMusic)
        binding.nameSong.text = nameAds.toString()
    }
}