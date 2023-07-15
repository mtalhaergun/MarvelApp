package com.mte.marvelapp.ui.home

import android.os.Bundle
import android.system.Os
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.databinding.FragmentHomeBinding
import com.mte.marvelapp.ui.home.adapter.CharacterAdapter
import com.mte.marvelapp.ui.home.adapter.listener.CharacterClickListener
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HomeViewModel by viewModels()
    private lateinit var characterAdapter: CharacterAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        viewModel.fetchCharacters()
        observeEvents()
    }

    private fun observeEvents() = with(binding){
        viewModel.characterUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is CharacterUiState.Loading -> {
                    pbHeroes.visibility = View.VISIBLE
                    rvHeroes.visibility = View.GONE
                }
                is CharacterUiState.Success -> {
                    state.data?.let {
                        pbHeroes.visibility = View.GONE
                        characterAdapter.characters = state.data
                        rvHeroes.visibility = View.VISIBLE
                    }
                }
                is CharacterUiState.Error -> {

                }
            }
        })
    }

    private fun setupRecyclerViews() = with(binding){
        characterAdapter = CharacterAdapter(object : CharacterClickListener{
            override fun onCharacterClick(character: Character) {

            }
        })
        rvHeroes.adapter = characterAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}