package com.rickandmorty.ui.episode.details.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickandmorty.data.Resource
import com.rickandmorty.data.remote.model.Episode
import com.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import com.rickandmorty.ui.adapter.LinksAdapter
import com.rickandmorty.ui.base.BaseFragment
import com.rickandmorty.ui.main.MainViewModel
import com.task.ui.episode.details.viewmodel.EpisodeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EpisodeDetailsFragment : BaseFragment<FragmentEpisodeDetailsBinding>(FragmentEpisodeDetailsBinding::inflate) {

    private val viewModel: EpisodeDetailsViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()
    lateinit var linksAdapter: LinksAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = EpisodeDetailsFragmentArgs.fromBundle(it)
            viewModel.getEpisode(args.episodeUrl)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linksAdapter = LinksAdapter()
        setupRecyclerView()
        setupObservable()
    }

    private fun setupObservable() {

        lifecycleScope.launchWhenStarted {
            viewModel.episode.collect { state ->
                when(state){
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        state.data?.let {
                            displayEpisode(it)
                            showLoading(false)
                        }
                    }

                    is Resource.DataError ->{
                        showLoading(false)
                    }

                }

            }
        }
    }

    private fun setupRecyclerView() {

        with(binding.characterRecyclerView) {
            layoutManager = LinearLayoutManager(activity)
            adapter = linksAdapter
        }


    }

    private fun showLoading(show: Boolean) {
        sharedViewModel._displayLoading.value = show


    }


    private fun displayEpisode(episode: Episode) {

        binding.episodeName.text = episode.name
        binding.episodeAireDate.text = episode.airDate
        linksAdapter.setItems(episode.characters)

    }





}