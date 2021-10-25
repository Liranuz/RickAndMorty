package com.task.ui.character.list.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.material.checkbox.MaterialCheckBox
import com.task.R
import com.task.data.remote.model.CharacterData
import com.task.databinding.CharactersFragmentBinding
import com.task.ui.main.MainViewModel
import com.task.ui.base.BaseFragment
import com.task.ui.character.list.view.adapter.CharactersAdapter
import com.task.ui.character.list.viewmodel.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharactersListFragment : BaseFragment<CharactersFragmentBinding>(CharactersFragmentBinding::inflate) {

    private val charactersViewModel: CharactersListViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()

    private lateinit var charactersAdapter: CharactersAdapter
    private var charactersJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        charactersAdapter = CharactersAdapter()
        setupRecyclerView()
        getCharactersJob()

    }

    private fun getCharactersJob() {
        with(charactersViewModel) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                charactersFlow.collectLatest { charactersAdapter.submitData(it) }
            }
        }
    }

    private fun setupRecyclerView() {

        with(binding.charactersRecyclerView) {
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
            charactersAdapter.onItemClick = { onItemClickListener(it) }
            adapter = charactersAdapter
        }


        charactersAdapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {

                if (charactersAdapter.snapshot().isEmpty()) {
                    sharedViewModel._displayLoading.value = true
                }
                sharedViewModel._displayLoading.value = false

            } else {
                sharedViewModel._displayLoading.value = false

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
//                    if (charactersAdapter.snapshot().isEmpty()) {
//                        binding.errorTextView.isVisible = true
//                        binding.errorTextView.text = it.error.localizedMessage
//                    }
                }

            }
        }
    }

    private fun onItemClickListener(character: CharacterData) {
        val direction =
            CharactersListFragmentDirections.actionCharactersListFragmentToCharacterDetailsFragment(
                character
            )
        findNavController().navigate(direction)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_button -> {
                showSearchDialog()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSearchDialog(): Boolean {

        activity?.let { activity ->
            val dialog = MaterialDialog(activity, MaterialDialog.DEFAULT_BEHAVIOR)
                .title(R.string.dialog_title)
                .negativeButton(text = getString(R.string.dialog_negative_button)) {
                    it.dismiss()
                }
                .noAutoDismiss()
                .customView(
                    viewRes = R.layout.dialog_filter_character,
                    scrollable = true
                )
            val dialogView = dialog.getCustomView()

            val statusCategory = listOf(
                dialogView.findViewById(R.id.status_alive),
                dialogView.findViewById(R.id.status_dead),
                dialogView.findViewById<MaterialCheckBox>(R.id.status_unknown)
            )
            val genderCategory = listOf(
                dialogView.findViewById(R.id.gender_female),
                dialogView.findViewById(R.id.gender_male),
                dialogView.findViewById(R.id.gender_genderless),
                dialogView.findViewById<MaterialCheckBox>(R.id.gender_unknown)
            )
            val speciesCustom = listOf(
                dialogView.findViewById(R.id.species_human),
                dialogView.findViewById(R.id.species_humanoid),
                dialogView.findViewById(R.id.species_alien),
                dialogView.findViewById(R.id.species_animal),
                dialogView.findViewById(R.id.species_robot),
                dialogView.findViewById(R.id.species_poopy),
                dialogView.findViewById<MaterialCheckBox>(R.id.species_cronenberg)
            )

            //restoreCheckBoxState(dialogView)

//            cbSpeciesAll.setOnCheckedChangeListener { _, isChecked ->
//                if (isChecked) {
//                    speciesCustom.forEach {
//                        it.isChecked = false
//                    }
//                }
//            }
//            speciesCustom.forEach {
//                it.setOnCheckedChangeListener { _, isChecked ->
//                    if (isChecked && cbSpeciesAll.isChecked) {
//                        cbSpeciesAll.isChecked = false
//                    }
//                }
//            }

            dialog.positiveButton(text = getString(R.string.dialog_positive_button)) { mdialog ->
                if (
                    statusCategory.any { it.isChecked } &&
                    genderCategory.any { it.isChecked } &&
                    speciesCustom.any { it.isChecked }) {
                    setupFiltration(mdialog)
                } else {
                    val errors: MutableList<String> = mutableListOf()
                    if (statusCategory.all { !it.isChecked }) {
                        errors.add(getString(R.string.dialog_error_status))
                    }
                    if (genderCategory.all { !it.isChecked }) {
                        errors.add(getString(R.string.dialog_error_gender))
                    }
                    if (speciesCustom.all { !it.isChecked }) {
                        errors.add(getString(R.string.dialog_error_species))
                    }
//                        activity.displayErrorDialog(
//                            getString(R.string.dialog_error_message)
//                                .plus(errors.joinToString())
//                        )
                }
            }

            dialog.show {
                lifecycleOwner(viewLifecycleOwner)
            }

        }

        return false
    }


    private fun setupFiltration(mdialog: MaterialDialog) {

        val dialogView = mdialog.getCustomView()
        val filterMap = mutableMapOf<String, String>()
        var status = ""
        var gender = ""
        var species = ""

        //according to the state of a checkbox map the appropriate booleans and string values
        if (dialogView.findViewById<MaterialCheckBox>(R.id.status_alive).isChecked) {
            status = dialogView.findViewById<MaterialCheckBox>(R.id.status_alive).text.toString()
        } else if(dialogView.findViewById<MaterialCheckBox>(R.id.status_dead).isChecked) {
            status = dialogView.findViewById<MaterialCheckBox>(R.id.status_dead).text.toString()
        } else {
            status = dialogView.findViewById<MaterialCheckBox>(R.id.status_unknown).text.toString()
        }

        if(status.isNotEmpty()) {
            filterMap["status"] = status
        }

        if (dialogView.findViewById<MaterialCheckBox>(R.id.gender_female).isChecked) {
           gender = "female"
        } else if (dialogView.findViewById<MaterialCheckBox>(R.id.gender_male).isChecked) {
           gender = "male"
        } else if(dialogView.findViewById<MaterialCheckBox>(R.id.gender_genderless).isChecked){
            gender = "genderless"
        } else {
            gender = "unknown"
        }

        if(gender.isNotEmpty()) {
            filterMap["gender"] = gender
        }

        if (dialogView.findViewById<MaterialCheckBox>(R.id.species_human).isChecked) {
            species = "human"
        } else if (dialogView.findViewById<MaterialCheckBox>(R.id.species_humanoid).isChecked) {
            species = "humanoid"
        } else if(dialogView.findViewById<MaterialCheckBox>(R.id.species_animal).isChecked){
            species = "animal"
        } else if(dialogView.findViewById<MaterialCheckBox>(R.id.species_robot).isChecked){
            species = "robot"
        } else if(dialogView.findViewById<MaterialCheckBox>(R.id.species_poopy).isChecked){
            species = "poopybutthole"
        } else if(dialogView.findViewById<MaterialCheckBox>(R.id.species_cronenberg).isChecked){
            species = "cronenberg"
        } else if (dialogView.findViewById<MaterialCheckBox>(R.id.species_alien).isChecked) {
            species = "alien"
        }

        if(species.isNotEmpty()) {
            filterMap["species"] = species
        }

        charactersViewModel.getFilterCharacters(filterMap)
        getCharactersJob()


//        with(charactersViewModel) {
//            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
//                charactersFlow.collectLatest { charactersAdapter.submitData(it) }
//            }
//        }


//        charactersJob?.cancel()
//        charactersJob = lifecycleScope.launch {
//            charactersViewModel.setFilter(filterMap1)
//                .collectLatest {
//                    charactersAdapter.submitData(it)
//                }
//
//        }
        mdialog.dismiss()


    }

}









