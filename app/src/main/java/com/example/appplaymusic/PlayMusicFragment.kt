package com.example.appplaymusic

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.appplaymusic.adapter.SongViewPagerAdapter
import com.example.appplaymusic.databinding.FragmentPlayMusicBinding
import com.example.appplaymusic.model.DataListMusic
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class PlayMusicFragment : Fragment() {
    lateinit var binding: FragmentPlayMusicBinding
    lateinit var songList: DataListMusic
    private var position: Int = 0
    private var player: MediaPlayer? = null
    private var autoRefreshDisposable: Disposable? = null
    private val playMusicViewModel: PlayMusicViewModel by viewModels()
    private lateinit var songViewPagerAdapter: SongViewPagerAdapter

    companion object {
        fun newInstance(dataListMusic: DataListMusic, position: Int): PlayMusicFragment {
            val args = Bundle()
            args.putSerializable("dataListMusic", dataListMusic)
            args.putInt("position", position)
            val fragment = PlayMusicFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun initializePlayer() {
        player = MediaPlayer.create(requireContext(), songList[position].linkBaiHat)
        player?.start()
        setTimeTotal()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayMusicBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initializePlayer()
        initEvent()
        initViewModels()
    }

    private fun initViewModels() {
        playMusicViewModel.apply {
            isPlaying.observe(viewLifecycleOwner) {
                if (it) {
                    player?.start()
                    binding.btnStop.setImageResource(R.drawable.ic_pause)
                } else {
                    player?.stop()
                    binding.btnStop.setImageResource(R.drawable.ic_play)
                }
            }
            positionSong.observe(viewLifecycleOwner) {
                position = when {
                    it > songList.size - 1 -> { //nếu songList có 6 bài position quay về 0
                        0
                    }
                    it < 0 -> {
                        songList.size - 1
                    }
                    else -> {
                        it
                    }
                }
                if (player?.isPlaying == true) {
                    player?.stop()
                }
                createExoPlayerMusic()
                binding.btnStop.setImageResource(R.drawable.ic_pause)
                binding.seekBarSong.progress = 0
                player?.start()
            }
        }
    }


    private fun updateTime() {
        autoRefreshDisposable?.dispose()
        autoRefreshDisposable =
            Observable.interval(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateView()
                }, {
                })
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateView() {
        val dateFormat = SimpleDateFormat("mm:ss")
        binding.timeBd.text = dateFormat.format(player?.currentPosition!!)
        binding.seekBarSong.progress = (player?.currentPosition?.toInt()!!) / 1000

    }

    private fun createExoPlayerMusic() { //khởi tạo 1 mediaPlayer với vị trí bài hát thứ position trong songList
        player?.stop()
        val url = songList[position].linkBaiHat
        player = MediaPlayer.create(requireContext(), url)
        player?.start()
        binding.tvNameSong.text = songList[position].tenBaiHat
    }

    fun changeSong(positionChange: Int) {
        position = positionChange

        playMusicViewModel.changeSong(positionChange)
        if (player?.isPlaying == true) {
            player?.stop()
        }
        createExoPlayerMusic()
        binding.btnStop.setImageResource(R.drawable.ic_pause)
        binding.seekBarSong.progress = 0
        player?.start()
    }

    private fun initEvent() {
        binding.btnStop.setOnClickListener {
            if (player?.isPlaying == true) { //nếu đang hát->pause-> đổi hình nút pause
                playMusicViewModel.stopPlay()
                player?.stop()
            } else { // nếu đang dừng hát
                playMusicViewModel.startPlay()
                player?.start()
            }
        }
        binding.btnNext.setOnClickListener {
            player?.stop()
            playMusicViewModel.stopPlay()
            playMusicViewModel.nextSong(position, songList.size)
        }
        binding.btnPrevious.setOnClickListener {
            playMusicViewModel.stopPlay()
            playMusicViewModel.previousSong(position, songList.size)
        }
        binding.seekBarSong.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                player?.seekTo((binding.seekBarSong.progress.toLong() * 1000).toInt())
            }
        })
        binding.btnBackListMusic.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.tvNameSong.setOnClickListener { return@setOnClickListener }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setTimeTotal() { //dùng để gán progress max của seekbar bằng tgian bài hát Duration.
        val dateFormat = SimpleDateFormat("mm:ss")
        binding.timeKt.text = dateFormat.format(player?.duration ?: 0)
        binding.seekBarSong.max = (player?.duration?.toInt() ?: 0) / 1000
    }

    private fun initView() {
        songList = requireArguments().getSerializable("dataListMusic") as DataListMusic
        position = requireArguments().getInt("position")
        songViewPagerAdapter = SongViewPagerAdapter(this)
        songViewPagerAdapter.addSongList(songList, position)
        binding.viewPageSong.adapter = songViewPagerAdapter

        createExoPlayerMusic()
        updateTime()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        player = null
    }
}