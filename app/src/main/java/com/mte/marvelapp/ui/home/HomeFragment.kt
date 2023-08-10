package com.mte.marvelapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.transition.TransitionManager
import com.mte.marvelapp.R
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.data.remote.model.comic.Comic
import com.mte.marvelapp.data.remote.model.enums.Category
import com.mte.marvelapp.data.remote.model.event.Events
import com.mte.marvelapp.data.remote.model.series.Series
import com.mte.marvelapp.data.remote.model.stories.Stories
import com.mte.marvelapp.databinding.FragmentHomeBinding
import com.mte.marvelapp.ui.adapter.itemadapter.CharacterAdapter
import com.mte.marvelapp.ui.adapter.listadapter.CharacterRecyclerAdapter
import com.mte.marvelapp.ui.adapter.itemadapter.ComicsAdapter
import com.mte.marvelapp.ui.adapter.listadapter.ComicsRecyclerAdapter
import com.mte.marvelapp.ui.adapter.itemadapter.EventsAdapter
import com.mte.marvelapp.ui.adapter.listadapter.EventsRecyclerAdapter
import com.mte.marvelapp.ui.adapter.itemadapter.SeriesAdapter
import com.mte.marvelapp.ui.adapter.listadapter.SeriesRecyclerAdapter
import com.mte.marvelapp.ui.adapter.itemadapter.StoriesAdapter
import com.mte.marvelapp.ui.adapter.listadapter.StoriesRecyclerAdapter
import com.mte.marvelapp.ui.adapter.listener.CharacterClickListener
import com.mte.marvelapp.ui.adapter.listener.ComicClickListener
import com.mte.marvelapp.ui.adapter.listener.EventsClickListener
import com.mte.marvelapp.ui.adapter.listener.SeeAllClickListener
import com.mte.marvelapp.ui.adapter.listener.SeriesClickListener
import com.mte.marvelapp.ui.adapter.listener.StoriesClickListener
import com.mte.marvelapp.utils.extensions.isInternetConnected
import com.mte.marvelapp.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HomeViewModel by viewModels()

    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var comicsAdapter: ComicsAdapter
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var eventsAdapter : EventsAdapter

    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var characterRecyclerAdapter: CharacterRecyclerAdapter
    private lateinit var seriesRecyclerAdapter: SeriesRecyclerAdapter
    private lateinit var comicsRecyclerAdapter: ComicsRecyclerAdapter
    private lateinit var storiesRecyclerAdapter: StoriesRecyclerAdapter
    private lateinit var eventsRecyclerAdapter : EventsRecyclerAdapter

    private var isDarkMode : SharedPreferences? = null

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
        setTheme()
        setupRecyclerViews()
        observeEvents()
        sendApiRequests()
    }

    private fun observeEvents() = with(binding){

        lifecycleScope.launch {
            viewModel.characters.collectLatest { characters ->
                if (characters != null) {
                    characterAdapter.submitData(characters)
                }
            }
        }

        characterAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.NotLoading) {
                characterRecyclerAdapter.stopShimmer()
            }
            else if(combinedLoadStates.refresh is LoadState.Error){
                apiRequestTimer({characterRecyclerAdapter.stopShimmer()},{viewModel.fetchCharacters()})
            }
        }

        lifecycleScope.launch{
            viewModel.series.collectLatest { series ->
                if (series != null) {
                    seriesAdapter.submitData(series)
                }
            }
        }

        seriesAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.NotLoading) {
                seriesRecyclerAdapter.stopShimmer()
            }
            else if(combinedLoadStates.refresh is LoadState.Error){
                apiRequestTimer({seriesRecyclerAdapter.stopShimmer()},{viewModel.fetchSeries()})
            }
        }

        lifecycleScope.launch{
            viewModel.comics.collectLatest { comics ->
                if (comics != null) {
                    comicsAdapter.submitData(comics)
                }
            }
        }

        comicsAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.NotLoading) {
                comicsRecyclerAdapter.stopShimmer()
            }
            else if(combinedLoadStates.refresh is LoadState.Error){
                apiRequestTimer({comicsRecyclerAdapter.stopShimmer()},{viewModel.fetchComics()})
            }
        }

        lifecycleScope.launch{
            viewModel.stories.collectLatest { stories ->
                if (stories != null) {
                    storiesAdapter.submitData(stories)
                }
            }
        }

        storiesAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.NotLoading) {
                storiesRecyclerAdapter.stopShimmer()
            }
            else if(combinedLoadStates.refresh is LoadState.Error){
                apiRequestTimer({storiesRecyclerAdapter.stopShimmer()},{viewModel.fetchStories()})
            }
        }

        lifecycleScope.launch{
            viewModel.events.collectLatest { events ->
                if (events != null) {
                    eventsAdapter.submitData(events)
                }
            }
        }

        eventsAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.refresh is LoadState.NotLoading) {
                eventsRecyclerAdapter.stopShimmer()
            }
            else if(combinedLoadStates.refresh is LoadState.Error){
                apiRequestTimer({eventsRecyclerAdapter.stopShimmer()},{viewModel.fetchEvents()})
            }
        }

        binding.themeIcon.setOnClickListener {
            val isDark = isDarkMode?.getBoolean("theme",false)
            if(isDark != true){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.themeIcon.setImageResource(R.drawable.icon_light_mode)
                isDarkMode?.edit()?.putBoolean("theme",true)?.apply()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.themeIcon.setImageResource(R.drawable.icon_dark_mode)
                isDarkMode?.edit()?.putBoolean("theme",false)?.apply()
            }
        }
    }

    private fun setupAdapters(){
        characterAdapter = CharacterAdapter(object : CharacterClickListener {
            override fun onCharacterClick(character: Character) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(character.id.toString(),Category.CHARACTERS)
                findNavController().safeNavigate(action)
            }
        })

        seriesAdapter = SeriesAdapter(object : SeriesClickListener {
            override fun onSeriesClick(series: Series) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(series.id.toString(),Category.SERIES)
                findNavController().safeNavigate(action)
            }
        })

        comicsAdapter = ComicsAdapter(object : ComicClickListener {
            override fun onComicClick(comic: Comic) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(comic.id.toString(),Category.COMICS)
                findNavController().safeNavigate(action)
            }
        })

        storiesAdapter = StoriesAdapter(object : StoriesClickListener {
            override fun onStoriesClick(stories: Stories) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(stories.id.toString(),Category.STORIES)
                findNavController().safeNavigate(action)
            }
        })

        eventsAdapter = EventsAdapter(object : EventsClickListener {
            override fun onEventsClick(events: Events) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(events.id.toString(),Category.EVENTS)
                findNavController().safeNavigate(action)
            }
        })

        characterRecyclerAdapter = CharacterRecyclerAdapter(characterAdapter, object :
            SeeAllClickListener {
            override fun onSeeAllClick(category: String) {
                val action = HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(Category.CHARACTERS)
                findNavController().safeNavigate(action)
            }
        })

        seriesRecyclerAdapter = SeriesRecyclerAdapter(seriesAdapter, object : SeeAllClickListener {
            override fun onSeeAllClick(category: String) {
                val action = HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(Category.SERIES)
                findNavController().safeNavigate(action)
            }
        })

        comicsRecyclerAdapter = ComicsRecyclerAdapter(comicsAdapter, object : SeeAllClickListener {
            override fun onSeeAllClick(category: String) {
                val action = HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(Category.COMICS)
                findNavController().safeNavigate(action)
            }
        })

        storiesRecyclerAdapter = StoriesRecyclerAdapter(storiesAdapter, object :
            SeeAllClickListener {
            override fun onSeeAllClick(category: String) {
                // API'da Stories için search query'si olmadığından See All sayfası yapılamıyor
            }
        })

        eventsRecyclerAdapter = EventsRecyclerAdapter(eventsAdapter, object : SeeAllClickListener {
            override fun onSeeAllClick(category: String) {
                val action = HomeFragmentDirections.actionHomeFragmentToSeeAllFragment(Category.EVENTS)
                findNavController().safeNavigate(action)
            }
        })

        concatAdapter = ConcatAdapter(
            characterRecyclerAdapter,
            seriesRecyclerAdapter,
            comicsRecyclerAdapter,
            storiesRecyclerAdapter,
            eventsRecyclerAdapter
        )

    }

    private fun setupRecyclerViews() = with(binding){
        rvCategories.adapter = concatAdapter
    }

    private fun sendApiRequests() = with(viewModel){
        fetchCharacters()
        fetchSeries()
        fetchComics()
        fetchStories()
        fetchEvents()
    }

    private fun apiRequestTimer(stopShimmer : () -> Unit, fetchCategoryFunction: () -> Unit){
        val interval = 10000L

        val job = lifecycleScope.launch {
            while (isActive) {
                if (requireContext().isInternetConnected()) {
                    stopShimmer()
                    break
                }
                delay(interval)
            }
            fetchCategoryFunction()
        }
    }

    private fun setTheme(){
        isDarkMode = requireActivity().getSharedPreferences("theme", Context.MODE_PRIVATE)
        val isDark = isDarkMode?.getBoolean("theme",false)
        if(isDark == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.themeIcon.setImageResource(R.drawable.icon_light_mode)
            isDarkMode?.edit()?.putBoolean("theme",true)?.apply()
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.themeIcon.setImageResource(R.drawable.icon_dark_mode)
            isDarkMode?.edit()?.putBoolean("theme",false)?.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            val isDark = isDarkMode?.getBoolean("theme",false)
            WindowCompat.setDecorFitsSystemWindows(it.window, false)
            val insetsController = WindowInsetsControllerCompat(it.window, it.window.decorView)
            insetsController.isAppearanceLightStatusBars = isDark != true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}