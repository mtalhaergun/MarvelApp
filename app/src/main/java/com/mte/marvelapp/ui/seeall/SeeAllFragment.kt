package com.mte.marvelapp.ui.seeall

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import com.mte.marvelapp.R
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.databinding.FragmentSeeAllBinding
import com.mte.marvelapp.ui.home.HomeFragmentDirections
import com.mte.marvelapp.ui.home.adapter.CharacterAdapter
import com.mte.marvelapp.ui.home.adapter.listener.CharacterClickListener
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import com.mte.marvelapp.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class SeeAllFragment : Fragment() {

    private var _binding : FragmentSeeAllBinding? = null
    private val binding get () = _binding!!

    private val viewModel : SeeAllViewModel by viewModels()

    private lateinit var characterAdapter: CharacterAdapter

    private val args : SeeAllFragmentArgs by navArgs()

    var selectedCategory : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapters()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSeeAllBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedCategory = args.category
        setupRecyclerViews()
        viewModel.fetchCharacters()
        observeEvents()
    }

    private fun setupAdapters(){
        characterAdapter = CharacterAdapter(object : CharacterClickListener {
            override fun onCharacterClick(character: Character) {
//                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(character.id.toString(),"characters")
//                findNavController().safeNavigate(action)
            }
        })
    }

    private fun setupRecyclerViews() = with(binding) {
        if (selectedCategory == "characters") {
            binding.rvSeeAll.adapter = characterAdapter
        } else if (selectedCategory == "series") {

        } else if (selectedCategory == "comics") {

        } else if (selectedCategory == "events") {

        }
    }


    private fun observeEvents() = with(binding) {

        lifecycleScope.launch {
            viewModel.characters.collectLatest { characters ->
                if (characters != null) {
                    characterAdapter.submitData(characters)
                }
            }
        }

        viewModel.characterUiState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is CharacterUiState.Loading -> {
//                    pbHeroes.visibility = View.VISIBLE
//                    rvHeroes.visibility = View.GONE
                }

                is CharacterUiState.Success -> {
//                    pbHeroes.visibility = View.GONE
//                    rvHeroes.visibility = View.VISIBLE
                }

                is CharacterUiState.Error -> {

                }
            }
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query != null && query != ""){
                    if (selectedCategory == "characters") {
                        viewModel.searchCharacters(query)
                    } else if (selectedCategory == "series") {

                    } else if (selectedCategory == "comics") {

                    } else if (selectedCategory == "events") {

                    }
                }else{
                    if (selectedCategory == "characters") {
                        viewModel.fetchCharacters()
                    } else if (selectedCategory == "series") {

                    } else if (selectedCategory == "comics") {

                    } else if (selectedCategory == "events") {

                    }
                }
                return true
            }

        })
    }
}