package com.task.ui.character.list.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import com.shevaalex.android.rickmortydatabase.utils.Const.Companion.GENDER
import com.shevaalex.android.rickmortydatabase.utils.Const.Companion.SPECIES
import com.shevaalex.android.rickmortydatabase.utils.Const.Companion.STATUS
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
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        charactersAdapter = CharactersAdapter()
        setupRecyclerView()
        getCharactersJob()

    }

    private fun getCharactersJob() {
        charactersJob?.cancel()
        charactersJob = lifecycleScope.launch {
            charactersViewModel.getAllCharacters()
                .collectLatest {
                    charactersAdapter.submitData(it)
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
                    if (charactersAdapter.snapshot().isEmpty()) {
                        Toast.makeText(requireActivity(), it.error.localizedMessage, Toast.LENGTH_LONG).show()
                    }
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

        val statusAlive = dialogView.findViewById<MaterialCheckBox>(R.id.status_alive)
        val statusDead = dialogView.findViewById<MaterialCheckBox>(R.id.status_dead)

        if (statusAlive.isChecked) {
            status = statusAlive.text.toString()

        } else if(statusDead.isChecked) {
            status = statusDead.text.toString()
        } else {
            status = dialogView.findViewById<MaterialCheckBox>(R.id.status_unknown).text.toString()
        }

        if(status.isNotEmpty()) {
            filterMap[STATUS] = status
        }

        val genderFemale = dialogView.findViewById<MaterialCheckBox>(R.id.gender_female)
        val genderMale = dialogView.findViewById<MaterialCheckBox>(R.id.gender_male)
        val genderless = dialogView.findViewById<MaterialCheckBox>(R.id.gender_genderless)


        if (genderFemale.isChecked) {
           gender = genderFemale.text.toString()
        } else if (genderMale.isChecked) {
           gender = genderMale.text.toString()
        } else if(dialogView.findViewById<MaterialCheckBox>(R.id.gender_genderless).isChecked){
            gender = genderless.text.toString()
        } else {
            gender = dialogView.findViewById<MaterialCheckBox>(R.id.gender_unknown).text.toString()
        }

        if(gender.isNotEmpty()) {
            filterMap[GENDER] = gender
        }

        val speciesHuman = dialogView.findViewById<MaterialCheckBox>(R.id.species_human)
        val speciesHumanoid = dialogView.findViewById<MaterialCheckBox>(R.id.species_humanoid)
        val speciesAnimal = dialogView.findViewById<MaterialCheckBox>(R.id.species_animal)
        val speciesRobot = dialogView.findViewById<MaterialCheckBox>(R.id.species_robot)
        val speciesPoopybutthole = dialogView.findViewById<MaterialCheckBox>(R.id.species_poopy)
        val speciesCronenberg = dialogView.findViewById<MaterialCheckBox>(R.id.species_cronenberg)
        val speciesAlien= dialogView.findViewById<MaterialCheckBox>(R.id.species_alien)


        if (speciesHuman.isChecked) {
            species = speciesHuman.text.toString()
        } else if (speciesHumanoid.isChecked) {
            species = speciesHumanoid.text.toString()
        } else if(speciesAnimal.isChecked){
            species = speciesAnimal.text.toString()
        } else if(speciesRobot.isChecked){
            species = speciesRobot.text.toString()
        } else if(speciesPoopybutthole.isChecked){
            species = speciesPoopybutthole.text.toString()
        } else if(speciesCronenberg.isChecked){
            species = speciesCronenberg.text.toString()
        } else if (speciesAlien.isChecked) {
            species = speciesAlien.text.toString()
        }

        if(species.isNotEmpty()) {
            filterMap[SPECIES] = species
        }

        charactersJob?.cancel()
        charactersJob = lifecycleScope.launch {
            charactersViewModel.getFilterCharacters(filterMap)
                .collectLatest {
                    charactersAdapter.submitData(it)
                }
        }

        mdialog.dismiss()


    }

}









