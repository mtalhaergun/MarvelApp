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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.R
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.data.remote.model.comic.Comic
import com.mte.marvelapp.data.remote.model.event.Events
import com.mte.marvelapp.data.remote.model.series.Series
import com.mte.marvelapp.databinding.FragmentSeeAllBinding
import com.mte.marvelapp.ui.home.HomeFragmentDirections
import com.mte.marvelapp.ui.home.adapter.CharacterAdapter
import com.mte.marvelapp.ui.home.adapter.ComicsAdapter
import com.mte.marvelapp.ui.home.adapter.EventsAdapter
import com.mte.marvelapp.ui.home.adapter.SeriesAdapter
import com.mte.marvelapp.ui.home.adapter.StoriesAdapter
import com.mte.marvelapp.ui.home.adapter.listener.CharacterClickListener
import com.mte.marvelapp.ui.home.adapter.listener.ComicClickListener
import com.mte.marvelapp.ui.home.adapter.listener.EventsClickListener
import com.mte.marvelapp.ui.home.adapter.listener.SeriesClickListener
import com.mte.marvelapp.utils.extensions.capitalize
import com.mte.marvelapp.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class SeeAllFragment : Fragment() {

    private var _binding : FragmentSeeAllBinding? = null
    private val binding get () = _binding!!

    private val viewModel : SeeAllViewModel by viewModels()

    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var comicsAdapter: ComicsAdapter
    private lateinit var eventsAdapter : EventsAdapter

    private val args : SeeAllFragmentArgs by navArgs()

    var selectedCategory : String? = null
    private var searchJob: Job? = null
    var apiRequestOnce = false
    var tempSearchQuery : String? = null

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
        if(!apiRequestOnce) {
            sendApiRequests()
            apiRequestOnce = true
        }
        observeEvents()
        setItemDecoration()
        setTitle()
    }

    private fun setupAdapters(){
        characterAdapter = CharacterAdapter(object : CharacterClickListener {
            override fun onCharacterClick(character: Character) {
                val action = SeeAllFragmentDirections.actionSeeAllFragmentToDetailsFragment(character.id.toString(),selectedCategory)
                findNavController().safeNavigate(action)
            }
        })

        seriesAdapter = SeriesAdapter(object : SeriesClickListener {
            override fun onSeriesClick(series: Series) {
                val action = SeeAllFragmentDirections.actionSeeAllFragmentToDetailsFragment(series.id.toString(),selectedCategory)
                findNavController().safeNavigate(action)
            }
        })

        comicsAdapter = ComicsAdapter(object : ComicClickListener {
            override fun onComicClick(comic: Comic) {
                val action = SeeAllFragmentDirections.actionSeeAllFragmentToDetailsFragment(comic.id.toString(),selectedCategory)
                findNavController().safeNavigate(action)
            }
        })

        eventsAdapter = EventsAdapter(object : EventsClickListener {
            override fun onEventsClick(events: Events) {
                val action = SeeAllFragmentDirections.actionSeeAllFragmentToDetailsFragment(events.id.toString(),selectedCategory)
                findNavController().safeNavigate(action)
            }
        })
    }

    private fun setupRecyclerViews() = with(binding) {
        if (selectedCategory == "characters") {
            binding.rvSeeAll.adapter = characterAdapter
        } else if (selectedCategory == "series") {
            binding.rvSeeAll.adapter = seriesAdapter
        } else if (selectedCategory == "comics") {
            binding.rvSeeAll.adapter = comicsAdapter
        } else if (selectedCategory == "events") {
            binding.rvSeeAll.adapter = eventsAdapter
        }
    }


    private fun observeEvents() = with(binding) {

        lifecycleScope.launch {
            viewModel.characters.collectLatest { characters ->
                if (characters != null) {
                    startShimmer()
                    characterAdapter.submitData(characters)
                }
            }
        }

        characterAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.NotLoading) {
                stopShimmer()
            }
        }

        lifecycleScope.launch {
            viewModel.series.collectLatest { series ->
                if (series != null) {
                    startShimmer()
                    seriesAdapter.submitData(series)
                }
            }
        }

        seriesAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.NotLoading) {
                stopShimmer()
            }
        }

        lifecycleScope.launch {
            viewModel.comics.collectLatest { comics ->
                if (comics != null) {
                    startShimmer()
                    comicsAdapter.submitData(comics)
                }
            }
        }

        comicsAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.NotLoading) {
                stopShimmer()
            }
        }

        lifecycleScope.launch {
            viewModel.events.collectLatest { events ->
                if (events != null) {
                    startShimmer()
                    eventsAdapter.submitData(events)
                }
            }
        }

        eventsAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.NotLoading) {
                stopShimmer()
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                searchJob?.cancel()
                if(query != null && query != ""){
                    if(tempSearchQuery != query){
                        searchJob = lifecycleScope.launch(Dispatchers.Main) {
                            delay(500)
                            if (selectedCategory == "characters") {
                                viewModel.searchCharacters(query)
                            } else if (selectedCategory == "series") {
                                viewModel.searchSeries(query)
                            } else if (selectedCategory == "comics") {
                                viewModel.searchComics(query)
                            } else if (selectedCategory == "events") {
                                viewModel.searchEvents(query)
                            }
                        }
                    }
                }else{
                    if (selectedCategory == "characters") {
                        viewModel.fetchCharacters()
                    } else if (selectedCategory == "series") {
                        viewModel.fetchSeries()
                    } else if (selectedCategory == "comics") {
                        viewModel.fetchComics()
                    } else if (selectedCategory == "events") {
                        viewModel.fetchEvents()
                    }
                }
                tempSearchQuery = query
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
            viewModel.fetchSeries()
        }
        else if (selectedCategory == "comics"){
            viewModel.fetchComics()
        }
        else if (selectedCategory == "events"){
            viewModel.fetchEvents()
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

    private fun startShimmer(){
        binding.rvSeeAll.visibility = View.GONE
        binding.seeallShimmerInclude.root.visibility = View.VISIBLE
        binding.seeallShimmerInclude.shimmerSeeallLayout.startShimmer()
    }

    private fun stopShimmer(){
        binding.seeallShimmerInclude.root.visibility = View.GONE
        binding.seeallShimmerInclude.shimmerSeeallLayout.stopShimmer()
        binding.rvSeeAll.visibility = View.VISIBLE
    }
}