package com.example.appplaymusic

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appplaymusic.adapter.SongViewPagerAdapter
import com.example.appplaymusic.databinding.FragmentPlayMusicBinding
import com.example.appplaymusic.model.DataListMusic


class PlayMusicFragment : Fragment() {
    lateinit var binding: FragmentPlayMusicBinding
    lateinit var songList: DataListMusic
    private var position: Int = 0
    private val playMusicViewModel: PlayMusicViewModel by viewModels()
    private lateinit var songViewPagerAdapter: SongViewPagerAdapter
    private var ACTION_PAUSE: Int = 1
    private var ACTION_PlAY: Int = 2
    private var ACTION_NEXT: Int = 3
    private var ACTION_PREVIOUS: Int = 4
    private var ACTION_CHOOSE: Int = 5
    private var ACTION_SEEK_MUSIC: Int = 6
    private var ACTION_SYNC_MUSIC: Int = 7
    private var ACTION_RANDOM_MUSIC: Int = 8
    private var isPlayingMusic: Boolean = false

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
        initEvent()
        initViewModels()
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val timeKt = intent.getStringExtra("timeKt")
                    val seekBarSongProgress = intent.getIntExtra("seekBarSongProgress", 0)
                    setTimeTotal(timeKt ?: "", seekBarSongProgress)
                    playMusicViewModel.startPlay()
                }
            }, IntentFilter("setTimeTotal"))
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val timeUpdate = intent.getStringExtra("timeUpdate")
                    val seekBarProgressUpdate = intent.getIntExtra("seekBarProgressUpdate", 0)
                    isPlayingMusic = intent.getBooleanExtra("isPlayingMusic", false)
                    updateView(timeUpdate, seekBarProgressUpdate)
                }
            }, IntentFilter("upDateTime"))
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    playMusicViewModel.startPlay()
                }
            }, IntentFilter("playMusic"))
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    playMusicViewModel.stopPlay()
                }
            }, IntentFilter("pauseMusic"))
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateView(timeUpdate: String?, seekBarProgressUpdate: Int) {
        binding.timeBd.text = timeUpdate
        binding.seekBarSong.progress = seekBarProgressUpdate
        binding.tvNameSong.text = songList[position].tenBaiHat
    }

    @SuppressLint("SimpleDateFormat")
    private fun setTimeTotal(
        timeKt: String,
        seekBarSongProgress: Int
    ) { //dùng để gán progress max của seekbar bằng tgian bài hát Duration.
        binding.timeKt.text = timeKt
        binding.seekBarSong.max = seekBarSongProgress
    }

    private fun initViewModels() {
        playMusicViewModel.apply {
            isPlaying.observe(viewLifecycleOwner) {
                if (it) {
                    sendActionService(ACTION_PlAY, position)
                    binding.btnStop.setImageResource(R.drawable.ic_pause)
                } else {
                    sendActionService(ACTION_PAUSE, position)
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
                if (isPlayingMusic) {
                    sendActionService(ACTION_PAUSE, position)
                }
                binding.btnStop.setImageResource(R.drawable.ic_pause)
                binding.seekBarSong.progress = 0
                sendActionService(ACTION_PlAY, position)
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

    private fun btnStartService(data: DataListMusic, position: Int) {
        val intent = Intent(requireActivity(), MusicService::class.java)
        val bundle = Bundle()
        bundle.putSerializable("dataMusicListSong", data)
        bundle.putInt("positionMusic", position)
        intent.putExtras(bundle)
        requireActivity().startService(intent)
    }

    private fun sendActionService(actionMusic: Int, position: Int) {
        val intentService = Intent(context, MusicService::class.java)
        intentService.putExtra("action_music_service", actionMusic)
        intentService.putExtra("positionSongMusic", position)
        context?.startService(intentService)
    }

    fun changeSong(positionChange: Int) {
        position = positionChange

        playMusicViewModel.changeSong(positionChange)
        if (isPlayingMusic) {
            sendActionService(ACTION_PAUSE, position)
        }
        binding.btnStop.setImageResource(R.drawable.ic_pause)
        binding.seekBarSong.progress = 0
        sendActionService(ACTION_CHOOSE, positionChange)
    }

    private fun initEvent() {
        binding.btnStop.setOnClickListener {
            if (isPlayingMusic) {
                playMusicViewModel.stopPlay()
            } else { // nếu đang dừng hát
                playMusicViewModel.startPlay()
            }
        }
        binding.btnNext.setOnClickListener {
            playMusicViewModel.stopPlay()
            playMusicViewModel.nextSong(position, songList.size)
            sendActionService(ACTION_NEXT, position)
        }
        binding.btnPrevious.setOnClickListener {
            playMusicViewModel.stopPlay()
            playMusicViewModel.previousSong(position, songList.size)
            sendActionService(ACTION_PREVIOUS, position)
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
                sendActionService(
                    ACTION_SEEK_MUSIC,
                    (binding.seekBarSong.progress.toLong() * 1000).toInt()
                )
            }
        })
        binding.imgMiss.setOnClickListener {
            sendNotification()
            position = songList.size - 1 //chuyen den bai hat muon phat
            if (isPlayingMusic) {
                sendActionService(ACTION_PAUSE, position)
            }
            sendActionService(ACTION_PlAY, position)
            binding.btnStop.setImageResource(R.drawable.ic_pause)
            binding.seekBarSong.progress = 0
        }
        binding.btnBackListMusic.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.tvNameSong.setOnClickListener { return@setOnClickListener }
        binding.btnSync.setOnClickListener {
            playMusicViewModel.syncMusic()
            sendActionService(ACTION_SYNC_MUSIC, position)
        }
        binding.btnRandomMusic.setOnClickListener {
            playMusicViewModel.randomMusic()
        }

    }
    private fun sendNotification() {
        (activity as MainActivity).sendNotification()
    }
    private fun initView() {
        songList = requireArguments().getSerializable("dataListMusic") as DataListMusic
        position = requireArguments().getInt("position")
        songViewPagerAdapter = SongViewPagerAdapter(this)
        songViewPagerAdapter.addSongList(songList, position)
        binding.viewPageSong.adapter = songViewPagerAdapter
        btnStartService(songList, position)
    }

}