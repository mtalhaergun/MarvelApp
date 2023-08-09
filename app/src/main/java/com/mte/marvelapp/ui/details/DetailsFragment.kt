package com.mte.marvelapp.ui.details

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
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
import com.mte.marvelapp.data.remote.model.enums.Category
import com.mte.marvelapp.data.remote.model.event.Events
import com.mte.marvelapp.data.remote.model.series.Series
import com.mte.marvelapp.data.remote.model.stories.Stories
import com.mte.marvelapp.databinding.FragmentDetailsBinding
import com.mte.marvelapp.ui.adapter.itemadapter.CharacterAdapter
import com.mte.marvelapp.ui.adapter.itemadapter.ComicsAdapter
import com.mte.marvelapp.ui.adapter.itemadapter.CreatorsAdapter
import com.mte.marvelapp.ui.adapter.itemadapter.EventsAdapter
import com.mte.marvelapp.ui.adapter.itemadapter.SeriesAdapter
import com.mte.marvelapp.ui.adapter.itemadapter.StoriesAdapter
import com.mte.marvelapp.ui.adapter.listener.CharacterClickListener
import com.mte.marvelapp.ui.adapter.listener.ComicClickListener
import com.mte.marvelapp.ui.adapter.listener.CreatorsClickListener
import com.mte.marvelapp.ui.adapter.listener.EventsClickListener
import com.mte.marvelapp.ui.adapter.listener.SeriesClickListener
import com.mte.marvelapp.ui.adapter.listener.StoriesClickListener
import com.mte.marvelapp.utils.extensions.isInternetConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
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
        characterAdapter = CharacterAdapter(object : CharacterClickListener {
            override fun onCharacterClick(character: Character) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(character.id.toString(),Category.CHARACTERS)
                findNavController().navigate(action)
            }
        })

        seriesAdapter = SeriesAdapter(object : SeriesClickListener {
            override fun onSeriesClick(series: Series) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(series.id.toString(),Category.SERIES)
                findNavController().navigate(action)
            }
        })

        comicsAdapter = ComicsAdapter(object : ComicClickListener {
            override fun onComicClick(comic: Comic) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(comic.id.toString(),Category.COMICS)
                findNavController().navigate(action)
            }
        })

        storiesAdapter = StoriesAdapter(object : StoriesClickListener {
            override fun onStoriesClick(stories: Stories) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(stories.id.toString(),Category.STORIES)
                findNavController().navigate(action)
            }
        })

        eventsAdapter = EventsAdapter(object : EventsClickListener {
            override fun onEventsClick(events: Events) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(events.id.toString(),Category.EVENTS)
                findNavController().navigate(action)
            }
        })

        creatorsAdapter = CreatorsAdapter(object : CreatorsClickListener {
            override fun onCreatorsClick(creators: Creator) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(creators.id.toString(),Category.CREATORS)
                findNavController().navigate(action)
            }
        })

    }

    private fun observeEvents() = with(binding){

        viewModel.characterResponse.observe(viewLifecycleOwner, Observer {model ->
            model?.data?.characters?.getOrNull(0)?.let { character ->
                stopShimmer()

                with(character) {
                    val imageUrl = "${thumbnail?.path}/portrait_uncanny.${thumbnail?.extension}"
                    glideWithListener(detailImage, imageUrl)

                    detailId.text = id.toString()
                    detailTitle.text = name
                    detailDescription.text = description
                    rvTitle.text = "Series"
                    statsCharacter.visibility = View.GONE
                    statsSeries.text = series?.available.toString()
                    statsComics.text = comics?.available.toString()
                    statsStories.text = stories?.available.toString()
                    statsEvents.text = events?.available.toString()

                    setCategoryAnimation(categoryHeroIcon)

                    val color = ContextCompat.getColor(requireContext(), R.color.primary_red)
                    categoryHeroIcon.setColorFilter(color)
                    statsCharacterName.setTextColor(color)

                    val seriesTextList = createStatsBar(series?.available)
                    seriesStatsCount.text = spanTextColors(seriesTextList[0], seriesTextList[1])
                    val comicsTextList = createStatsBar(comics?.available)
                    comicsStatsCount.text = spanTextColors(comicsTextList[0], comicsTextList[1])
                    val storiesTextList = createStatsBar(stories?.available)
                    storiesStatsCount.text = spanTextColors(storiesTextList[0], storiesTextList[1])
                    val eventsTextList = createStatsBar(events?.available)
                    eventsStatsCount.text = spanTextColors(eventsTextList[0], eventsTextList[1])

                    characterStatsBar.visibility = View.GONE
                }
            } ?: startShimmer()
        })

        viewModel.seriesResponse.observe(viewLifecycleOwner, Observer {model ->
            model?.data?.series?.getOrNull(0)?.let { series ->
                stopShimmer()

                with(series){
                    val imageUrl = "${thumbnail?.path}/portrait_uncanny.${thumbnail?.extension}"
                    glideWithListener(detailImage, imageUrl)

                    detailId.text = id.toString()
                    detailTitle.text = title
                    detailDescription.text = description
                    rvTitle.text = "Stories"
                    statsSeries.visibility = View.GONE
                    statsCharacter.text = characters?.available.toString()
                    statsComics.text = comics?.available.toString()
                    statsStories.text = stories?.available.toString()
                    statsEvents.text = events?.available.toString()

                    setCategoryAnimation(categoryVillainIcon)

                    val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                    categoryVillainIcon.setColorFilter(color)
                    statsSeriesName.setTextColor(color)

                    val textListCharacters = createStatsBar(characters?.available)
                    characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                    val textListComics = createStatsBar(comics?.available)
                    comicsStatsCount.text = spanTextColors(textListComics[0],textListComics[1])
                    val textListStories = createStatsBar(stories?.available)
                    storiesStatsCount.text = spanTextColors(textListStories[0],textListStories[1])
                    val textListEvents = createStatsBar(events?.available)
                    eventsStatsCount.text = spanTextColors(textListEvents[0],textListEvents[1])

                    seriesStatsBar.visibility = View.GONE
                }
            } ?: startShimmer()
        })

        viewModel.comicResponse.observe(viewLifecycleOwner, Observer {model ->
            model?.data?.comics?.getOrNull(0)?.let { comics ->
                stopShimmer()

                with(comics){
                    var imageUrl = "${thumbnail?.path}/portrait_uncanny.${thumbnail?.extension}"
                    glideWithListener(detailImage,imageUrl)

                    detailId.text = id.toString()
                    detailTitle.text = title
                    detailDescription.text = description
                    rvTitle.text = "Creators"
                    statsComics.visibility = View.GONE
                    statsCharacter.text = characters?.available.toString()
                    statsSeries.text = "1"
                    statsStories.text = stories?.available.toString()
                    statsEvents.text = events?.available.toString()

                    setCategoryAnimation(categoryAntiheroIcon)

                    val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                    categoryAntiheroIcon.setColorFilter(color)
                    statsComicsName.setTextColor(color)

                    val textListCharacters = createStatsBar(characters?.available)
                    characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                    val textListSeries = createStatsBar(1)
                    seriesStatsCount.text = spanTextColors(textListSeries[0],textListSeries[1])
                    val textListStories = createStatsBar(stories?.available)
                    storiesStatsCount.text = spanTextColors(textListStories[0],textListStories[1])
                    val textListEvents = createStatsBar(events?.available)
                    eventsStatsCount.text = spanTextColors(textListEvents[0],textListEvents[1])

                    comicsStatsBar.visibility = View.GONE
                }
            } ?: startShimmer()
        })

        viewModel.storiesResponse.observe(viewLifecycleOwner, Observer {model ->
            model?.data?.stories?.getOrNull(0)?.let { stories ->
                stopShimmer()

                with(stories){
                    var imageUrl = "${thumbnail?.path}/portrait_uncanny.${thumbnail?.extension}"
                    glideWithListener(detailImage,imageUrl)

                    detailId.text = id.toString()
                    detailTitle.text = title
                    detailDescription.text = description
                    rvTitle.text = "Comics"
                    statsStories.visibility = View.GONE
                    statsCharacter.text = characters?.available.toString()
                    statsComics.text = comics?.available.toString()
                    statsSeries.text = series?.available.toString()
                    statsEvents.text = events?.available.toString()

                    setCategoryAnimation(categoryAlienIcon)

                    val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                    categoryAlienIcon.setColorFilter(color)
                    statsStoriesName.setTextColor(color)

                    val textListCharacters = createStatsBar(characters?.available)
                    characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                    val textListSeries = createStatsBar(series?.available)
                    seriesStatsCount.text = spanTextColors(textListSeries[0],textListSeries[1])
                    val textListComics = createStatsBar(comics?.available)
                    comicsStatsCount.text = spanTextColors(textListComics[0],textListComics[1])
                    val textListEvents = createStatsBar(events?.available)
                    eventsStatsCount.text = spanTextColors(textListEvents[0],textListEvents[1])

                    storiesStatsBar.visibility = View.GONE
                }
            } ?: startShimmer()
        })

        viewModel.eventResponse.observe(viewLifecycleOwner, Observer {model ->
            model?.data?.events?.getOrNull(0)?.let { events ->
                stopShimmer()
                with(events){
                    var imageUrl = "${thumbnail?.path}/portrait_uncanny.${thumbnail?.extension}"
                    glideWithListener(detailImage,imageUrl)

                    detailId.text = id.toString()
                    detailTitle.text = title
                    detailDescription.text = description
                    rvTitle.text = "Characters"
                    statsEvents.visibility = View.GONE
                    statsCharacter.text = characters?.available.toString()
                    statsComics.text = comics?.available.toString()
                    statsSeries.text = series?.available.toString()
                    statsStories.text = stories?.available.toString()

                    setCategoryAnimation(categoryHumanIcon)

                    val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                    categoryHumanIcon.setColorFilter(color)
                    statsEventsName.setTextColor(color)

                    val textListCharacters = createStatsBar(characters?.available)
                    characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                    val textListSeries = createStatsBar(series?.available)
                    seriesStatsCount.text = spanTextColors(textListSeries[0],textListSeries[1])
                    val textListComics = createStatsBar(comics?.available)
                    comicsStatsCount.text = spanTextColors(textListComics[0],textListComics[1])
                    val textListStories = createStatsBar(stories?.available)
                    storiesStatsCount.text = spanTextColors(textListStories[0],textListStories[1])

                    eventsStatsBar.visibility = View.GONE
                }

            } ?: startShimmer()
        })

        viewModel.creatorResponse.observe(viewLifecycleOwner, Observer {model ->
            model?.data?.creators?.getOrNull(0)?.let { creators ->
                stopShimmer()

                with(creators){
                    var imageUrl = "${thumbnail?.path}/portrait_uncanny.${thumbnail?.extension}"
                    glideWithListener(detailImage,imageUrl)

                    detailId.text = id.toString()
                    detailTitle.text = fullName
                    detailDescription.text = ""
                    rvTitle.text = "Events"
                    statsCharacter.text = "0"
                    statsEvents.text = events?.available.toString()
                    statsComics.text = comics?.available.toString()
                    statsSeries.text = series?.available.toString()
                    statsStories.text = stories?.available.toString()

                    val textListCharacters = createStatsBar(0)
                    characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                    val textListSeries = createStatsBar(series?.available)
                    seriesStatsCount.text = spanTextColors(textListSeries[0],textListSeries[1])
                    val textListComics = createStatsBar(comics?.available)
                    comicsStatsCount.text = spanTextColors(textListComics[0],textListComics[1])
                    val textListStories = createStatsBar(stories?.available)
                    storiesStatsCount.text = spanTextColors(textListStories[0],textListStories[1])
                    val textListEvents = createStatsBar(events?.available)
                    eventsStatsCount.text = spanTextColors(textListEvents[0],textListEvents[1])

                }
            } ?: startShimmer()
        })

        lifecycleScope.launch{
            viewModel.characters.collectLatest { characters ->
                if (characters != null) {
                    characterAdapter.submitData(characters)
                }
            }
        }

        lifecycleScope.launch{
            viewModel.series.collectLatest { series ->
                if (series != null) {
                    seriesAdapter.submitData(series)
                }
            }
        }

        lifecycleScope.launch{
            viewModel.comics.collectLatest { comics ->
                if (comics != null) {
                    comicsAdapter.submitData(comics)
                }
            }
        }

        lifecycleScope.launch{
            viewModel.stories.collectLatest { stories ->
                if (stories != null) {
                    storiesAdapter.submitData(stories)
                }
            }
        }

        lifecycleScope.launch{
            viewModel.events.collectLatest { events ->
                if (events != null) {
                    eventsAdapter.submitData(events)
                }
            }
        }

        lifecycleScope.launch{
            viewModel.creators.collectLatest { creators ->
                if (creators != null) {
                    creatorsAdapter.submitData(creators)
                }
            }
        }

        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun sendApiRequests() = with(viewModel){
        if(requireContext().isInternetConnected()){
            args.id?.let {id ->
                val selectedCategory = args.category
                when(selectedCategory){

                    Category.CHARACTERS -> {
                        fetchCharacterDetail(id)
                        fetchCharactersSeries(id)
                        binding.rvDetail.adapter = seriesAdapter
                    }

                    Category.SERIES -> {
                        fetchSeriesDetail(id)
                        fetchSeriesStories(id)
                        binding.rvDetail.adapter = storiesAdapter
                    }

                    Category.COMICS -> {
                        fetchComicDetail(id)
                        fetchComicsCreators(id)
                        binding.rvDetail.adapter = creatorsAdapter
                    }

                    Category.STORIES -> {
                        fetchStoriesDetail(id)
                        fetchStoriesComics(id)
                        binding.rvDetail.adapter = comicsAdapter
                    }

                    Category.EVENTS -> {
                        fetchEventDetail(id)
                        fetchEventsCharacters(id)
                        binding.rvDetail.adapter = characterAdapter
                    }

                    Category.CREATORS -> {
                        fetchCreatorDetail(id)
                        fetchCreatorsEvents(id)
                        binding.rvDetail.adapter = eventsAdapter
                    }
                }

            }
        }else{
            startShimmer()
            apiRequestTimer()
        }
    }

    private fun changeToolbarColor(){
        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.scrollView.scrollY
            val alpha = (scrollY / 1500f).coerceIn(0f, 1f)
            val alphaInt = (alpha * 255).toInt()
            binding.toolbar.setBackgroundColor(android.graphics.Color.argb(alphaInt, 0, 0, 0))
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

    private fun createStatsBar(stat : Int?) : ArrayList<String>{
        var textBar : String = ""
        var textBarEnd : String = ""
        var textList = arrayListOf<String>()
        if(stat != null && stat != 0){
            for(i in 0 until stat/5){
                textBar += "ı "
                if(i == 28) break
            }
            textBar += "| "

            for(i in 1..30 - textBar.length/2){
                textBarEnd += "ı "
            }
        }else{
            for(i in 1..30){
                textBarEnd += "ı "
            }
        }

        textList.add(textBar)
        textList.add(textBarEnd)

        return textList
    }

    private fun spanTextColors(text1 : String, text2 : String) : SpannableString{
        val spannableString = text1 + text2
        val spannableText = SpannableString(text1 + text2)

        spannableText.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            text1.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableText.setSpan(
            ForegroundColorSpan(Color.GRAY),
            text1.length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableText
    }

    private fun apiRequestTimer(){
        val interval = 3000L

        val job = lifecycleScope.launch {
            while (isActive) {
                if (requireContext().isInternetConnected()) {
                    stopShimmer()
                    break
                }
                delay(interval)
            }
            sendApiRequests()
        }
    }

    private fun setCategoryAnimation(categoryImage: View){
        val scaleAnimation = AnimatorInflater.loadAnimator(context,R.animator.detail_animation)
        scaleAnimation.setTarget(categoryImage)
        scaleAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animation.start()
            }
        })
        scaleAnimation.start()
    }

    private fun startShimmer(){
        binding.scrollView.visibility = View.GONE
        binding.detailShimmerInclude.root.visibility = View.VISIBLE
        binding.detailShimmerInclude.shimmerDetailLayout.startShimmer()
    }

    private fun stopShimmer(){
        binding.scrollView.visibility = View.VISIBLE
        binding.detailShimmerInclude.root.visibility = View.GONE
        binding.detailShimmerInclude.shimmerDetailLayout.stopShimmer()
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            WindowCompat.setDecorFitsSystemWindows(it.window, false)
            val insetsController = WindowInsetsControllerCompat(it.window, it.window.decorView)
            insetsController.isAppearanceLightStatusBars = false
        }
    }

}