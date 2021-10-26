package com.task.ui.character.details.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.task.data.remote.model.CharacterData
import com.task.databinding.CharacterDetailFragmentBinding
import com.task.ui.base.BaseFragment
import com.task.ui.adapter.LinksAdapter
import com.task.ui.character.details.viewmodel.CharacterDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment : BaseFragment<CharacterDetailFragmentBinding>(CharacterDetailFragmentBinding::inflate) {

    private val viewModel: CharacterDetailViewModel by viewModels()
    lateinit var linksAdapter: LinksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireArguments().let {
            val args = CharacterDetailFragmentArgs.fromBundle(it)
            linksAdapter = LinksAdapter()
            setupRecyclerView()
            showCharacter(args.characterModel)
        }
    }

    private fun setupRecyclerView() {

        with(binding.episodesRecyclerView) {
            layoutManager = LinearLayoutManager(activity)
            linksAdapter.onItemClick = { onItemClickListener(it) }
            adapter = linksAdapter
        }


    }

    private fun onItemClickListener(episodeUrl: String) {
        val direction = CharacterDetailFragmentDirections.actionCharacterDetailsFragmentToEpisodeDetailsFragment(episodeUrl)
        findNavController().navigate(direction)
    }

    private fun showCharacter(character: CharacterData) {
        binding.name.text = character.name
        binding.species.text = character.species
        binding.status.text = character.status
        binding.gender.text = character.gender
        linksAdapter.setItems(character.episode)
        Glide.with(binding.root)
            .load(character.image)
            .transform(CircleCrop())
            .into(binding.image)



    }



}
