package com.example.appplaymusic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appplaymusic.databinding.FragmentRoundDiscBinding
import com.example.appplaymusic.model.DataListMusic


class RoundDiscFragment : Fragment() {
    private lateinit var binding: FragmentRoundDiscBinding
    private var mPosition: Int? = null
    private var dataListMusic = DataListMusic()

    private val playMusicViewModel: PlayMusicViewModel by viewModels({
        requireParentFragment()
    })

    companion object {
        fun newInstance(dataListMusic: DataListMusic, mPosition: Int): RoundDiscFragment {
            val args = Bundle()
            args.putSerializable("dataMusicList", dataListMusic)
            args.putInt("mPosition", mPosition)
            val fragment = RoundDiscFragment()
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
        binding = FragmentRoundDiscBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        dataListMusic = requireArguments().getSerializable("dataMusicList") as DataListMusic
        mPosition = requireArguments().getInt("mPosition")
        val imgWaitLoader = RequestOptions().placeholder(R.drawable.ic_audacity).centerCrop()
        Glide.with(this).load(imgWaitLoader).apply(imgWaitLoader)
            .into(binding.imgRoundDisc)
    }

    private fun initViewModels() {
        playMusicViewModel.apply {
            isPlaying.observe(viewLifecycleOwner) {
                if (it) {
                    startAnimation(binding.imgRoundDisc)
                } else {
                    stopAnimation(binding.imgRoundDisc)
                }
            }
            positionSong.observe(viewLifecycleOwner) {
                val imgWaitLoader = RequestOptions().placeholder(R.drawable.ic_audacity).centerCrop()
                Glide.with(requireContext()).load(imgWaitLoader).apply(imgWaitLoader)
                    .into(binding.imgRoundDisc)
            }
        }
    }

    private fun startAnimation(view: View) {
        val runnable: Runnable = object : Runnable {
            override fun run() {
                view.animate().rotationBy(360f).withEndAction(this)
                    .setDuration(10000).setInterpolator(LinearInterpolator()).start()
            }
        }
        view.animate().rotationBy(360f).withEndAction(runnable)
            .setDuration(10000).setInterpolator(LinearInterpolator()).start()
    }

    private fun stopAnimation(view: View) { //dừng hiệu ứng
        view.animate().cancel()
    }
}