package com.example.appplaymusic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appplaymusic.PlayMusicFragment
import com.example.appplaymusic.R
import com.example.appplaymusic.adapter.SearchSongAdapter
import com.example.appplaymusic.databinding.FragmentSearchBinding
import com.example.appplaymusic.viewmodel.SearchViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    val publishSubject: PublishSubject<String> = PublishSubject.create()
    private lateinit var searchSongAdapter: SearchSongAdapter
    private val searchViewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        val layoutManager = LinearLayoutManager(context)
        searchSongAdapter = SearchSongAdapter(onItemClick = { dataListMusic, position ->
            val listMusic = PlayMusicFragment.newInstance(dataListMusic, position)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_content_id, listMusic)
                .addToBackStack("list_search").commit()
            searchSongAdapter.changeSelectedSong(position)
        })

        binding.rcvNumber.adapter = searchSongAdapter
        binding.rcvNumber.layoutManager = layoutManager
        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) publishSubject.onNext(newText)
                return false
            }
        })
        publishSubject.hide()
            .switchMap {
                if (it.isNotBlank()) {
                    Observable.just(it)
                        .delay(2000, TimeUnit.MILLISECONDS)
                } else {
                    Observable.just(it)
                }
            }
            .distinctUntilChanged()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.progressLoad.visibility = View.VISIBLE
                searchViewModel.callApi(it)
            }, {
                it.printStackTrace()
            }, {
            })
    }

    private fun initViewModels() {
        searchViewModel.apply {
            dataListSearchSong.observe(viewLifecycleOwner) {
                searchSongAdapter.addData(it)
            }
            actionShowCallApiFail.observe(viewLifecycleOwner) {
                Toast.makeText(context, "Call Api Fail", Toast.LENGTH_LONG).show()
            }
            loadSuccessProgressBar.observe(viewLifecycleOwner){
                binding.progressLoad.visibility = View.GONE
            }
        }
    }
}