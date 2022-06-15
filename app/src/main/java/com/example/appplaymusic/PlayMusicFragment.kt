package com.example.appplaymusic

import android.annotation.SuppressLint
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
import com.example.appplaymusic.model.DataListMusicItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class PlayMusicFragment : Fragment() {
    lateinit var binding: FragmentPlayMusicBinding
    lateinit var songList: DataListMusic
    private var position: Int = 0
    private var player: ExoPlayer? = null
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
        player = ExoPlayer.Builder(requireContext())
            .build()
            .also {
                it.playWhenReady = true
                it.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            playMusicViewModel.nextSong(position,songList.size)
                        } else if (playbackState == Player.STATE_READY) {
                            setTimeTotal()
                            playMusicViewModel.startPlay()
                        }
                    }
                })
            }
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
        initializePlayer()
        initView()
        initEvent()
        initViewModels()
    }

    private fun initViewModels() {
        playMusicViewModel.apply {
            isPlaying.observe(viewLifecycleOwner) {
                if (it) {
                    player?.play()
                    binding.btnStop.setImageResource(R.drawable.ic_pause)
                } else {
                    player?.pause()
                    binding.btnStop.setImageResource(R.drawable.ic_play)
                    btnStartService(songList[position])
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
                player?.play()
                btnStartService(songList[position])
            }
            actionShowCallApiFail.observe(viewLifecycleOwner) {
                Toast.makeText(context, "Call Api Fail", Toast.LENGTH_LONG).show()
            }
            isSyncMusic.observe(viewLifecycleOwner) {
                if (it) {
                    binding.btnSync.setImageResource(R.drawable.sync_active)
                } else {
                    binding.btnSync.setImageResource(R.drawable.sync_deactive)
                }
            }
            isRandomMusic.observe(viewLifecycleOwner) {
                if (it) {
                    binding.btnRandomMusic.setImageResource(R.drawable.ic_random_active)
                } else {
                    binding.btnRandomMusic.setImageResource(R.drawable.ic_random_deactive)
                }
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
        val url = songList[position].linkBaiHat.toString()
        player?.setMediaItem(MediaItem.fromUri(url))
        player?.prepare()
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
        player?.play()
        btnStartService(songList[position])
    }

    private fun initEvent() {
        binding.btnStop.setOnClickListener {
            if (player?.isPlaying == true) { //nếu đang hát->pause-> đổi hình nút pause
                playMusicViewModel.stopPlay()
            } else { // nếu đang dừng hát
                playMusicViewModel.startPlay()
            }
        }
        binding.btnNext.setOnClickListener {
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
                player?.seekTo(binding.seekBarSong.progress.toLong() * 1000)
            }
        })
        binding.imgMiss.setOnClickListener {
            sendNotification()
            position = songList.size - 1 //chuyen den bai hat muon phat
            if (player?.isPlaying == true) {
                player?.stop()
            }
            createExoPlayerMusic()
            player?.play()
            binding.btnStop.setImageResource(R.drawable.ic_pause)
            binding.seekBarSong.progress = 0
        }
        binding.btnBackListMusic.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.tvNameSong.setOnClickListener { return@setOnClickListener }
        binding.btnSync.setOnClickListener {
            playMusicViewModel.syncMusic()
        }
        binding.btnRandomMusic.setOnClickListener {
            playMusicViewModel.randomMusic()
        }

    }

    private fun btnStartService(dataListMusicItem: DataListMusicItem) {
        (activity as MainActivity).btnStartService(dataListMusicItem)
    }

    private fun sendNotification() {
        (activity as MainActivity).sendNotification()
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