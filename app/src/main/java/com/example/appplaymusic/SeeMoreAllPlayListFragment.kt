package com.example.appplaymusic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appplaymusic.adapter.SeeMoreAllPlayListAdapter
import com.example.appplaymusic.databinding.FragmentAllPlayListBinding
import com.example.appplaymusic.model.DataPlayList
import com.example.appplaymusic.viewmodel.SeeMoreAllPlayListViewModel

class SeeMoreAllPlayListFragment : Fragment() {
    private lateinit var binding: FragmentAllPlayListBinding
    private lateinit var dataPlayList: DataPlayList
    private lateinit var seeMoreAllListAdapter: SeeMoreAllPlayListAdapter
    private val seeMoreAllListViewModel: SeeMoreAllPlayListViewModel by viewModels()


    companion object {
        fun newInstance(
            dataPlayList: DataPlayList
        ): SeeMoreAllPlayListFragment {
            val args = Bundle()
            args.putSerializable("dataPlayList", dataPlayList)
            val fragment = SeeMoreAllPlayListFragment()
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
        binding = FragmentAllPlayListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initViewModels()
        dataPlayList = requireArguments().getSerializable("dataPlayList") as DataPlayList
        seeMoreAllListAdapter = SeeMoreAllPlayListAdapter(onItemClick = { dataPlayList, position ->
            seeMoreAllListViewModel.callApiPlayList(dataPlayList[position].idPlayList ?: "1")
            binding.loadMusicPlayList.visibility = View.VISIBLE
        })
        seeMoreAllListAdapter.addData(dataPlayList)
        binding.rcvAllPlayList.adapter = seeMoreAllListAdapter
        val gridLayoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rcvAllPlayList.layoutManager = gridLayoutManager
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initViewModels() {
        seeMoreAllListViewModel.apply {
            dataMusicPlayList.observe(viewLifecycleOwner) {
                val listMusic = PlayMusicFragment.newInstance(it,0)
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.activity_main_content_id, listMusic)
                    .addToBackStack("play_music").commit()
            }
            loadSuccessProgressBar.observe(viewLifecycleOwner) {
                binding.loadMusicPlayList.visibility = View.GONE
            }
            actionShowCallApiFail.observe(viewLifecycleOwner){
                Toast.makeText(context, "Call Api Fail", Toast.LENGTH_LONG).show()
            }
        }
    }
}