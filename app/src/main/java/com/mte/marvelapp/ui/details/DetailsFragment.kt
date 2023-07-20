package com.mte.marvelapp.ui.details

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mte.marvelapp.R
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.data.remote.model.comic.Comic
import com.mte.marvelapp.data.remote.model.creator.Creator
import com.mte.marvelapp.data.remote.model.event.Events
import com.mte.marvelapp.data.remote.model.series.Series
import com.mte.marvelapp.data.remote.model.stories.Stories
import com.mte.marvelapp.databinding.FragmentDetailsBinding
import com.mte.marvelapp.ui.home.adapter.CharacterAdapter
import com.mte.marvelapp.ui.home.adapter.ComicsAdapter
import com.mte.marvelapp.ui.home.adapter.CreatorsAdapter
import com.mte.marvelapp.ui.home.adapter.EventsAdapter
import com.mte.marvelapp.ui.home.adapter.SeriesAdapter
import com.mte.marvelapp.ui.home.adapter.StoriesAdapter
import com.mte.marvelapp.ui.home.adapter.listener.CharacterClickListener
import com.mte.marvelapp.ui.home.adapter.listener.ComicClickListener
import com.mte.marvelapp.ui.home.adapter.listener.CreatorsClickListener
import com.mte.marvelapp.ui.home.adapter.listener.EventsClickListener
import com.mte.marvelapp.ui.home.adapter.listener.SeriesClickListener
import com.mte.marvelapp.ui.home.adapter.listener.StoriesClickListener
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import com.mte.marvelapp.ui.home.uistate.ComicsUiState
import com.mte.marvelapp.ui.home.uistate.CreatorsUiState
import com.mte.marvelapp.ui.home.uistate.EventsUiState
import com.mte.marvelapp.ui.home.uistate.SeriesUiState
import com.mte.marvelapp.ui.home.uistate.StoriesUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding : FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : DetailsViewModel by viewModels()

    private val args : DetailsFragmentArgs by navArgs()

    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var comicsAdapter: ComicsAdapter
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var eventsAdapter : EventsAdapter
    private lateinit var creatorsAdapter: CreatorsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeToolbarColor()
        setupRecyclerView()
        sendApiRequests()
        observeEvents()
    }

    private fun setupRecyclerView() = with(binding) {
        characterAdapter = CharacterAdapter(object : CharacterClickListener{
            override fun onCharacterClick(character: Character) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(character.id.toString(),"characters")
                findNavController().navigate(action)
            }
        })

        seriesAdapter = SeriesAdapter(object : SeriesClickListener{
            override fun onSeriesClick(series: Series) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(series.id.toString(),"series")
                findNavController().navigate(action)
            }
        })

        comicsAdapter = ComicsAdapter(object : ComicClickListener{
            override fun onComicClick(comic: Comic) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(comic.id.toString(),"comics")
                findNavController().navigate(action)
            }
        })

        storiesAdapter = StoriesAdapter(object : StoriesClickListener{
            override fun onStoriesClick(stories: Stories) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(stories.id.toString(),"stories")
                findNavController().navigate(action)
            }
        })

        eventsAdapter = EventsAdapter(object : EventsClickListener{
            override fun onEventsClick(events: Events) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(events.id.toString(),"events")
                findNavController().navigate(action)
            }
        })

        creatorsAdapter = CreatorsAdapter(object : CreatorsClickListener{
            override fun onCreatorsClick(creators: Creator) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(creators.id.toString(),"creators")
                findNavController().navigate(action)
            }
        })

    }

    private fun observeEvents() = with(binding){

        viewModel.characterResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                with(model.data.characters[0]){
                    var imageUrl = this.thumbnail.path + "/portrait_uncanny." + this.thumbnail.extension
                    glideWithListener(detailImage,imageUrl)

                    detailTitle.text = this.name
                    detailDescription.text = this.description
                    rvTitle.text = "Series"
                    statsCharacter.visibility = View.GONE
                    statsSeries.text = this.series.available.toString()
                    statsComics.text = this.comics.available.toString()
                    statsStories.text = this.stories.available.toString()
                    statsEvents.text = this.events.available.toString()
                    val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                    categoryHeroIcon.setColorFilter(color)
                    statsCharacterName.setTextColor(color)
                }
            }
        })

        viewModel.seriesResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                with(model.data.series[0]){
                    var imageUrl = this.thumbnail.path + "/portrait_uncanny." + this.thumbnail.extension
                    glideWithListener(detailImage,imageUrl)

                    detailTitle.text = this.title
                    detailDescription.text = this.description
                    rvTitle.text = "Stories"
                    statsSeries.visibility = View.GONE
                    statsCharacter.text = this.characters.available.toString()
                    statsComics.text = this.comics.available.toString()
                    statsStories.text = this.stories.available.toString()
                    statsEvents.text = this.events.available.toString()
                    val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                    categoryVillainIcon.setColorFilter(color)
                    statsSeriesName.setTextColor(color)
                }
            }
        })

        viewModel.comicResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                with(model.data.comics[0]){
                    var imageUrl = this.thumbnail.path + "/portrait_uncanny." + this.thumbnail.extension
                    glideWithListener(detailImage,imageUrl)

                    detailTitle.text = this.title
                    detailDescription.text = this.description
                    rvTitle.text = "Creators"
                    statsComics.visibility = View.GONE
                    statsCharacter.text = this.characters.available.toString()
                    statsSeries.text = "1"
                    statsStories.text = this.stories.available.toString()
                    statsEvents.text = this.events.available.toString()
                    val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                    categoryAntiheroIcon.setColorFilter(color)
                    statsComicsName.setTextColor(color)
                }
            }
        })

        viewModel.storiesResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                with(model.data.stories[0]){
                    var imageUrl = this.thumbnail?.path + "/portrait_uncanny." + this.thumbnail?.extension
                    glideWithListener(detailImage,imageUrl)

                    detailTitle.text = this.title
                    detailDescription.text = this.description
                    rvTitle.text = "Comics"
                    statsStories.visibility = View.GONE
                    statsCharacter.text = this.characters.available.toString()
                    statsComics.text = this.comics.available.toString()
                    statsSeries.text = this.series.available.toString()
                    statsEvents.text = this.events.available.toString()
                    val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                    categoryAlienIcon.setColorFilter(color)
                    statsStoriesName.setTextColor(color)
                }
            }
        })

        viewModel.eventResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                with(model.data.events[0]){
                    var imageUrl = this.thumbnail.path + "/portrait_uncanny." + this.thumbnail.extension
                    glideWithListener(detailImage,imageUrl)

                    detailTitle.text = this.title
                    detailDescription.text = this.description
                    rvTitle.text = "Characters"
                    statsEvents.visibility = View.GONE
                    statsCharacter.text = this.characters.available.toString()
                    statsComics.text = this.comics.available.toString()
                    statsSeries.text = this.series.available.toString()
                    statsStories.text = this.stories.available.toString()
                    val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                    categoryHumanIcon.setColorFilter(color)
                    statsEventsName.setTextColor(color)
                }
            }
        })

        viewModel.creatorResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                with(model.data.creators[0]){
                    var imageUrl = this.thumbnail.path + "/portrait_uncanny." + this.thumbnail.extension
                    glideWithListener(detailImage,imageUrl)

                    detailTitle.text = this.fullName
                    detailDescription.text = ""
                    rvTitle.text = "Events"
                    statsCharacter.text = "0"
                    statsEvents.text = this.events.available.toString()
                    statsComics.text = this.comics.available.toString()
                    statsSeries.text = this.series.available.toString()
                    statsStories.text = this.stories.available.toString()
                }
            }
        })

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
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is CharacterUiState.Success -> {
                    pbRecycler.visibility = View.GONE
                    rvDetail.visibility = View.VISIBLE

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
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is SeriesUiState.Success -> {

                    pbRecycler.visibility = View.GONE
                    rvDetail.visibility = View.VISIBLE

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
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is ComicsUiState.Success -> {
                        pbRecycler.visibility = View.GONE
                        rvDetail.visibility = View.VISIBLE
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
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is StoriesUiState.Success -> {
                        pbRecycler.visibility = View.GONE
                        rvDetail.visibility = View.VISIBLE
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
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is EventsUiState.Success -> {
                        pbRecycler.visibility = View.GONE
                        rvDetail.visibility = View.VISIBLE
                }
                is EventsUiState.Error -> {

                }
            }
        })

        lifecycleScope.launch{
            viewModel.creators.collectLatest { creators ->
                if (creators != null) {
                    creatorsAdapter.submitData(creators)
                }
            }
        }

        viewModel.creatorsUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is CreatorsUiState.Loading -> {
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is CreatorsUiState.Success -> {
                        pbRecycler.visibility = View.GONE
                        rvDetail.visibility = View.VISIBLE
                }
                is CreatorsUiState.Error -> {

                }
            }
        })


        binding.iconBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun sendApiRequests() = with(viewModel){
        args.id?.let {id ->
            var selectedCategory = args.category
            if(selectedCategory == "characters"){
                fetchCharacterDetail(id)
                fetchCharactersSeries(id)
                binding.rvDetail.adapter = seriesAdapter
            }else if (selectedCategory == "series"){
                fetchSeriesDetail(id)
                fetchSeriesStories(id)
                binding.rvDetail.adapter = storiesAdapter
            }
            else if (selectedCategory == "comics"){
                fetchComicDetail(id)
                fetchComicsCreators(id)
                binding.rvDetail.adapter = creatorsAdapter
            }
            else if (selectedCategory == "stories"){
                fetchStoriesDetail(id)
                fetchStoriesComics(id)
                binding.rvDetail.adapter = comicsAdapter
            }
            else if (selectedCategory == "events"){
                fetchEventDetail(id)
                fetchEventsCharacters(id)
                binding.rvDetail.adapter = characterAdapter
            }else if (selectedCategory == "creators"){
                fetchCreatorDetail(id)
                fetchCreatorsEvents(id)
                binding.rvDetail.adapter = eventsAdapter
            }else{

            }
        }
    }

    private fun changeToolbarColor(){

        val endColor = Color.BLACK
        val startColor = ColorUtils.setAlphaComponent(endColor, 0)

        ViewCompat.setOnApplyWindowInsetsListener(binding.scrollView) { _, insets ->
            val scrollRange = binding.scrollView.getChildAt(0).measuredHeight - binding.scrollView.measuredHeight
            val scrollOffset = binding.scrollView.scrollY

            val progress = scrollOffset.toFloat() / scrollRange.toFloat()
            val color = ColorUtils.blendARGB(startColor, endColor, progress)

            binding.toolbar.setBackgroundColor(color)

            insets
        }

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollRange = binding.scrollView.getChildAt(0).measuredHeight - binding.scrollView.measuredHeight
            val scrollOffset = binding.scrollView.scrollY

            val progress = scrollOffset.toFloat() / scrollRange.toFloat()
            val color = ColorUtils.blendARGB(startColor, endColor, progress)

            binding.toolbar.setBackgroundColor(color)
        }
    }

    private fun glideWithListener(imageView : ImageView, url : String?){
        Glide.with(imageView).load(url).listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                imageView.setImageResource(R.drawable.image_not_available)
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        }).into(imageView)
    }

}