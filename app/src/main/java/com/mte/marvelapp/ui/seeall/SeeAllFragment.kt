package com.mte.marvelapp.ui.seeall

import android.graphics.Rect
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
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.R
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.databinding.FragmentSeeAllBinding
import com.mte.marvelapp.ui.home.HomeFragmentDirections
import com.mte.marvelapp.ui.home.adapter.CharacterAdapter
import com.mte.marvelapp.ui.home.adapter.listener.CharacterClickListener
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import com.mte.marvelapp.utils.extensions.capitalize
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
        selectedCategory = args.category
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
        setupRecyclerViews()
        sendApiRequests()
        observeEvents()
        setItemDecoration()
        setTitle()
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

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun sendApiRequests(){
        if(selectedCategory == "characters"){
            viewModel.fetchCharacters()
        }else if (selectedCategory == "series"){

        }
        else if (selectedCategory == "comics"){

        }
        else if (selectedCategory == "events"){

        }

    }

    inner class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount

            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }

    private fun setItemDecoration(){
        val spacing = resources.getDimensionPixelSize(R.dimen.recycler_item_spacing)
        binding.rvSeeAll.addItemDecoration(GridSpacingItemDecoration(2, spacing))
    }

    private fun setTitle(){
        binding.titleName = selectedCategory?.capitalize()
    }
}