package com.mte.marvelapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.mte.marvelapp.R
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.data.remote.model.comic.Comic
import com.mte.marvelapp.data.remote.model.event.Events
import com.mte.marvelapp.data.remote.model.series.Series
import com.mte.marvelapp.data.remote.model.stories.Stories
import com.mte.marvelapp.databinding.FragmentHomeBinding
import com.mte.marvelapp.ui.home.adapter.CharacterAdapter
import com.mte.marvelapp.ui.home.adapter.ComicsAdapter
import com.mte.marvelapp.ui.home.adapter.EventsAdapter
import com.mte.marvelapp.ui.home.adapter.HeaderAdapter
import com.mte.marvelapp.ui.home.adapter.SeriesAdapter
import com.mte.marvelapp.ui.home.adapter.StoriesAdapter
import com.mte.marvelapp.ui.home.adapter.listener.CharacterClickListener
import com.mte.marvelapp.ui.home.adapter.listener.ComicClickListener
import com.mte.marvelapp.ui.home.adapter.listener.EventsClickListener
import com.mte.marvelapp.ui.home.adapter.listener.SeriesClickListener
import com.mte.marvelapp.ui.home.adapter.listener.StoriesClickListener
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import com.mte.marvelapp.ui.home.uistate.ComicsUiState
import com.mte.marvelapp.ui.home.uistate.EventsUiState
import com.mte.marvelapp.ui.home.uistate.SeriesUiState
import com.mte.marvelapp.ui.home.uistate.StoriesUiState
import com.mte.marvelapp.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HomeViewModel by viewModels()

    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var comicsAdapter: ComicsAdapter
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var eventsAdapter : EventsAdapter
    private val headerCharacterAdapter by lazy { HeaderAdapter(getString(R.string.heroes_title))}
    private val headerSeriesAdapter by lazy { HeaderAdapter(getString(R.string.series_title))}
    private val headerComicsAdapter by lazy { HeaderAdapter(getString(R.string.comics_title))}
    private val headerStoriesAdapter by lazy { HeaderAdapter(getString(R.string.stories_title))}
    private val headerEventsAdapter by lazy { HeaderAdapter(getString(R.string.events_title))}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapters()
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
        observeEvents()
        sendApiRequests()
    }

    private fun observeEvents() = with(binding){

        lifecycleScope.launch{
            viewModel.characters.collectLatest { characters ->
                if (characters != null) {
                    characterAdapter.submitData(characters)
                }
            }
        }

        viewModel.characterUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is CharacterUiState.Loading -> {
                    pbHeroes.visibility = View.VISIBLE
                    rvHeroes.visibility = View.GONE
                }
                is CharacterUiState.Success -> {
                    pbHeroes.visibility = View.GONE
                    rvHeroes.visibility = View.VISIBLE
                }
                is CharacterUiState.Error -> {

                }
            }
        })

        lifecycleScope.launch{
            viewModel.series.collectLatest { series ->
                if (series != null) {
                    seriesAdapter.submitData(series)
                }
            }
        }

        viewModel.seriesUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is SeriesUiState.Loading -> {
                    pbSeries.visibility = View.VISIBLE
                    rvSeries.visibility = View.GONE
                }
                is SeriesUiState.Success -> {
                    pbSeries.visibility = View.GONE
                    rvSeries.visibility = View.VISIBLE
                }
                is SeriesUiState.Error -> {

                }
            }
        })

        lifecycleScope.launch{
            viewModel.comics.collectLatest { comics ->
                if (comics != null) {
                    comicsAdapter.submitData(comics)
                }
            }
        }

        viewModel.comicsUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is ComicsUiState.Loading -> {
                    pbComics.visibility = View.VISIBLE
                    rvComics.visibility = View.GONE
                }
                is ComicsUiState.Success -> {
                    pbComics.visibility = View.GONE
                    rvComics.visibility = View.VISIBLE
                }
                is ComicsUiState.Error -> {

                }
            }
        })

        lifecycleScope.launch{
            viewModel.stories.collectLatest { stories ->
                if (stories != null) {
                    storiesAdapter.submitData(stories)
                }
            }
        }

        viewModel.storiesUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is StoriesUiState.Loading -> {
                    pbStories.visibility = View.VISIBLE
                    rvStories.visibility = View.GONE
                }
                is StoriesUiState.Success -> {
                    pbStories.visibility = View.GONE
                    rvStories.visibility = View.VISIBLE
                }
                is StoriesUiState.Error -> {

                }
            }
        })

        lifecycleScope.launch{
            viewModel.events.collectLatest { events ->
                if (events != null) {
                    eventsAdapter.submitData(events)
                }
            }
        }

        viewModel.eventsUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is EventsUiState.Loading -> {
                    pbEvents.visibility = View.VISIBLE
                    rvEvents.visibility = View.GONE
                }
                is EventsUiState.Success -> {
                    pbEvents.visibility = View.GONE
                    rvEvents.visibility = View.VISIBLE
                }
                is EventsUiState.Error -> {

                }
            }
        })
    }

    private fun setupAdapters(){
        characterAdapter = CharacterAdapter(object : CharacterClickListener{
            override fun onCharacterClick(character: Character) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(character.id.toString(),"characters")
                findNavController().safeNavigate(action)
            }
        })

        seriesAdapter = SeriesAdapter(object : SeriesClickListener{
            override fun onSeriesClick(series: Series) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(series.id.toString(),"series")
                findNavController().safeNavigate(action)
            }
        })

        comicsAdapter = ComicsAdapter(object : ComicClickListener{
            override fun onComicClick(comic: Comic) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(comic.id.toString(),"comics")
                findNavController().safeNavigate(action)
            }

        })

        storiesAdapter = StoriesAdapter(object : StoriesClickListener{
            override fun onStoriesClick(stories: Stories) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(stories.id.toString(),"stories")
                findNavController().safeNavigate(action)
            }

        })

        eventsAdapter = EventsAdapter(object : EventsClickListener{
            override fun onEventsClick(events: Events) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(events.id.toString(),"events")
                findNavController().safeNavigate(action)
            }

        })
    }

    private fun setupRecyclerViews() = with(binding){

//        concatAdapter = ConcatAdapter(
//            headerCharacterAdapter,
//            characterAdapter,
//            headerSeriesAdapter,
//            seriesAdapter,
//            headerComicsAdapter,
//            comicsAdapter,
//            headerStoriesAdapter,
//            storiesAdapter,
//            headerEventsAdapter,
//            eventsAdapter
//        )
//
//        rvCategories.adapter = concatAdapter

        rvHeroes.adapter = characterAdapter
        rvSeries.adapter = seriesAdapter
        rvComics.adapter = comicsAdapter
        rvStories.adapter = storiesAdapter
        rvEvents.adapter = eventsAdapter
    }

    private fun sendApiRequests() = with(viewModel){
        fetchCharacters()
        fetchSeries()
        fetchComics()
        fetchStories()
        fetchEvents()
    }


    override fun onPause() {
        super.onPause()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}