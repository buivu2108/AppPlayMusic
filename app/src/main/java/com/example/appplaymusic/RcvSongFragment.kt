package com.example.appplaymusic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appplaymusic.adapter.RcvSongAdapter
import com.example.appplaymusic.databinding.FragmentRcvSongBinding
import com.example.appplaymusic.model.DataListMusic


class RcvSongFragment : Fragment() {
    private lateinit var binding: FragmentRcvSongBinding
    private lateinit var rcvSongAdapter: RcvSongAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var songList: DataListMusic
    private var position: Int = 0
    private val playMusicViewModel: PlayMusicViewModel by viewModels({
        requireParentFragment()
    })

    companion object {
        fun newInstance(dataListMusic: DataListMusic, position: Int): RcvSongFragment {
            val args = Bundle()
            args.putSerializable("dataRcvListMusic", dataListMusic)
            args.putInt("positionRcv", position)
            val fragment = RcvSongFragment()
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
        binding = FragmentRcvSongBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        songList = requireArguments().getSerializable("dataRcvListMusic") as DataListMusic
        position = requireArguments().getInt("positionRcv")
        layoutManager = LinearLayoutManager(context)
        rcvSongAdapter = RcvSongAdapter(onItemClick = { position ->
            rcvSongAdapter.changeSelectedSong(position)
            val playMusicFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.activity_main_content_id) as PlayMusicFragment
            playMusicFragment.changeSong(position)
        })
        rcvSongAdapter.addData(songList)
        rcvSongAdapter.changeSelectedSong(position)
        binding.rcvListSong.adapter = rcvSongAdapter
        binding.rcvListSong.layoutManager = layoutManager
        layoutManager.scrollToPositionWithOffset(position, 0)
    }

    private fun initViewModels() {
        playMusicViewModel.apply {
            positionSong.observe(viewLifecycleOwner) {
                layoutManager.scrollToPositionWithOffset(it, 0)
                rcvSongAdapter.changeSelectedSong(it)
            }
        }
    }
}