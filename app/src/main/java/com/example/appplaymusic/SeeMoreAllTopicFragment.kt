package com.example.appplaymusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appplaymusic.adapter.SeeMoreAllTopicAdapter
import com.example.appplaymusic.databinding.FragmentAllTopicListBinding
import com.example.appplaymusic.model.DataCategory
import com.example.appplaymusic.model.DataChuDe
import com.example.appplaymusic.viewmodel.SeeMoreAllTopicViewModel

class SeeMoreAllTopicFragment : Fragment() {
    private lateinit var binding: FragmentAllTopicListBinding
    private lateinit var dataCategory: DataCategory
    private lateinit var seeMoreAllTopicAdapter: SeeMoreAllTopicAdapter
    private val seeMoreAllTopicViewModel: SeeMoreAllTopicViewModel by viewModels()


    companion object {
        fun newInstance(
            dataCategory: DataCategory
        ): SeeMoreAllTopicFragment {
            val args = Bundle()
            args.putSerializable("dataCategory", dataCategory)
            val fragment = SeeMoreAllTopicFragment()
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
        binding = FragmentAllTopicListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initViewModels()
        dataCategory = requireArguments().getSerializable("dataCategory") as DataCategory
        seeMoreAllTopicAdapter = SeeMoreAllTopicAdapter(onItemClick = { dataCategory, position ->
               seeMoreAllTopicViewModel.getListMusicInCategory(dataCategory[position].idTheLoai.toString())
        })
        seeMoreAllTopicAdapter.addData(dataCategory)
        binding.rcvAllTopic.adapter = seeMoreAllTopicAdapter
        val gridLayoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rcvAllTopic.layoutManager = gridLayoutManager
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initViewModels() {
        seeMoreAllTopicViewModel.apply {
            dataMusicPlayList.observe(viewLifecycleOwner) {
                val listMusic = PlayMusicFragment.newInstance(it, 0)
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.activity_main_content_id, listMusic)
                    .addToBackStack("play_music").commit()
            }
            loadSuccessProgressBar.observe(viewLifecycleOwner) {
                binding.loadMusicTopic.visibility = View.GONE
            }
            actionShowCallApiFail.observe(viewLifecycleOwner) {
                Toast.makeText(context, "Call Api Fail", Toast.LENGTH_LONG).show()
            }
        }
    }
}