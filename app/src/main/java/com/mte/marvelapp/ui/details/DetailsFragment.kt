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
            if (model != null) {
                binding.scrollView.visibility = View.VISIBLE
                binding.detailShimmerInclude.root.visibility = View.GONE
                binding.detailShimmerInclude.shimmerDetailLayout.stopShimmer()
                if(model.data.characters != null){
                    with(model.data.characters[0]){
                        var imageUrl = this.thumbnail?.path + "/portrait_uncanny." + this.thumbnail?.extension
                        glideWithListener(detailImage,imageUrl)

                        detailId.text = this.id.toString()
                        detailTitle.text = this.name
                        detailDescription.text = this.description
                        rvTitle.text = "Series"
                        statsCharacter.visibility = View.GONE
                        statsSeries.text = this.series?.available.toString()
                        statsComics.text = this.comics?.available.toString()
                        statsStories.text = this.stories?.available.toString()
                        statsEvents.text = this.events?.available.toString()

                        setCategoryAnimation(categoryHeroIcon)

                        val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                        categoryHeroIcon.setColorFilter(color)
                        statsCharacterName.setTextColor(color)

                        val textListSeries = createStatsBar(this.series?.available)
                        seriesStatsCount.text = spanTextColors(textListSeries[0],textListSeries[1])
                        val textListComics = createStatsBar(this.comics?.available)
                        comicsStatsCount.text = spanTextColors(textListComics[0],textListComics[1])
                        val textListStories = createStatsBar(this.stories?.available)
                        storiesStatsCount.text = spanTextColors(textListStories[0],textListStories[1])
                        val textListEvents = createStatsBar(this.events?.available)
                        eventsStatsCount.text = spanTextColors(textListEvents[0],textListEvents[1])

                        characterStatsBar.visibility = View.GONE
                    }
                }
            }else{
                binding.scrollView.visibility = View.GONE
                binding.detailShimmerInclude.root.visibility = View.VISIBLE
                binding.detailShimmerInclude.shimmerDetailLayout.startShimmer()
            }
        })

        viewModel.seriesResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                binding.scrollView.visibility = View.VISIBLE
                binding.detailShimmerInclude.root.visibility = View.GONE
                binding.detailShimmerInclude.shimmerDetailLayout.stopShimmer()
                if(model.data.series != null){
                    with(model.data.series[0]){
                        var imageUrl = this.thumbnail?.path + "/portrait_uncanny." + this.thumbnail?.extension
                        glideWithListener(detailImage,imageUrl)

                        detailId.text = this.id.toString()
                        detailTitle.text = this.title
                        detailDescription.text = this.description
                        rvTitle.text = "Stories"
                        statsSeries.visibility = View.GONE
                        statsCharacter.text = this.characters?.available.toString()
                        statsComics.text = this.comics?.available.toString()
                        statsStories.text = this.stories?.available.toString()
                        statsEvents.text = this.events?.available.toString()

                        setCategoryAnimation(categoryVillainIcon)

                        val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                        categoryVillainIcon.setColorFilter(color)
                        statsSeriesName.setTextColor(color)

                        val textListCharacters = createStatsBar(this.characters?.available)
                        characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                        val textListComics = createStatsBar(this.comics?.available)
                        comicsStatsCount.text = spanTextColors(textListComics[0],textListComics[1])
                        val textListStories = createStatsBar(this.stories?.available)
                        storiesStatsCount.text = spanTextColors(textListStories[0],textListStories[1])
                        val textListEvents = createStatsBar(this.events?.available)
                        eventsStatsCount.text = spanTextColors(textListEvents[0],textListEvents[1])

                        seriesStatsBar.visibility = View.GONE
                    }
                }
            }else{
                binding.scrollView.visibility = View.GONE
                binding.detailShimmerInclude.root.visibility = View.VISIBLE
                binding.detailShimmerInclude.shimmerDetailLayout.startShimmer()
            }
        })

        viewModel.comicResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                binding.scrollView.visibility = View.VISIBLE
                binding.detailShimmerInclude.root.visibility = View.GONE
                binding.detailShimmerInclude.shimmerDetailLayout.stopShimmer()
                if(model.data.comics != null){
                    with(model.data.comics[0]){
                        var imageUrl = this.thumbnail?.path + "/portrait_uncanny." + this.thumbnail?.extension
                        glideWithListener(detailImage,imageUrl)

                        detailId.text = this.id.toString()
                        detailTitle.text = this.title
                        detailDescription.text = this.description
                        rvTitle.text = "Creators"
                        statsComics.visibility = View.GONE
                        statsCharacter.text = this.characters?.available.toString()
                        statsSeries.text = "1"
                        statsStories.text = this.stories?.available.toString()
                        statsEvents.text = this.events?.available.toString()

                        setCategoryAnimation(categoryAntiheroIcon)

                        val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                        categoryAntiheroIcon.setColorFilter(color)
                        statsComicsName.setTextColor(color)

                        val textListCharacters = createStatsBar(this.characters?.available)
                        characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                        val textListSeries = createStatsBar(1)
                        seriesStatsCount.text = spanTextColors(textListSeries[0],textListSeries[1])
                        val textListStories = createStatsBar(this.stories?.available)
                        storiesStatsCount.text = spanTextColors(textListStories[0],textListStories[1])
                        val textListEvents = createStatsBar(this.events?.available)
                        eventsStatsCount.text = spanTextColors(textListEvents[0],textListEvents[1])

                        comicsStatsBar.visibility = View.GONE
                    }
                }
            }else{
                binding.scrollView.visibility = View.GONE
                binding.detailShimmerInclude.root.visibility = View.VISIBLE
                binding.detailShimmerInclude.shimmerDetailLayout.startShimmer()
            }
        })

        viewModel.storiesResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                binding.scrollView.visibility = View.VISIBLE
                binding.detailShimmerInclude.root.visibility = View.GONE
                binding.detailShimmerInclude.shimmerDetailLayout.stopShimmer()
                if(model.data.stories != null){
                    with(model.data.stories[0]){
                        var imageUrl = this.thumbnail?.path + "/portrait_uncanny." + this.thumbnail?.extension
                        glideWithListener(detailImage,imageUrl)

                        detailId.text = this.id.toString()
                        detailTitle.text = this.title
                        detailDescription.text = this.description
                        rvTitle.text = "Comics"
                        statsStories.visibility = View.GONE
                        statsCharacter.text = this.characters?.available.toString()
                        statsComics.text = this.comics?.available.toString()
                        statsSeries.text = this.series?.available.toString()
                        statsEvents.text = this.events?.available.toString()

                        setCategoryAnimation(categoryAlienIcon)

                        val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                        categoryAlienIcon.setColorFilter(color)
                        statsStoriesName.setTextColor(color)

                        val textListCharacters = createStatsBar(this.characters?.available)
                        characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                        val textListSeries = createStatsBar(this.series?.available)
                        seriesStatsCount.text = spanTextColors(textListSeries[0],textListSeries[1])
                        val textListComics = createStatsBar(this.comics?.available)
                        comicsStatsCount.text = spanTextColors(textListComics[0],textListComics[1])
                        val textListEvents = createStatsBar(this.events?.available)
                        eventsStatsCount.text = spanTextColors(textListEvents[0],textListEvents[1])

                        storiesStatsBar.visibility = View.GONE
                    }
                }
            }else{
                binding.scrollView.visibility = View.GONE
                binding.detailShimmerInclude.root.visibility = View.VISIBLE
                binding.detailShimmerInclude.shimmerDetailLayout.startShimmer()
            }
        })

        viewModel.eventResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                binding.scrollView.visibility = View.VISIBLE
                binding.detailShimmerInclude.root.visibility = View.GONE
                binding.detailShimmerInclude.shimmerDetailLayout.stopShimmer()
                if(model.data.events != null){
                    with(model.data.events[0]){
                        var imageUrl = this.thumbnail?.path + "/portrait_uncanny." + this.thumbnail?.extension
                        glideWithListener(detailImage,imageUrl)

                        detailId.text = this.id.toString()
                        detailTitle.text = this.title
                        detailDescription.text = this.description
                        rvTitle.text = "Characters"
                        statsEvents.visibility = View.GONE
                        statsCharacter.text = this.characters?.available.toString()
                        statsComics.text = this.comics?.available.toString()
                        statsSeries.text = this.series?.available.toString()
                        statsStories.text = this.stories?.available.toString()

                        setCategoryAnimation(categoryHumanIcon)

                        val color = ContextCompat.getColor(requireContext(),R.color.primary_red)
                        categoryHumanIcon.setColorFilter(color)
                        statsEventsName.setTextColor(color)

                        val textListCharacters = createStatsBar(this.characters?.available)
                        characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                        val textListSeries = createStatsBar(this.series?.available)
                        seriesStatsCount.text = spanTextColors(textListSeries[0],textListSeries[1])
                        val textListComics = createStatsBar(this.comics?.available)
                        comicsStatsCount.text = spanTextColors(textListComics[0],textListComics[1])
                        val textListStories = createStatsBar(this.stories?.available)
                        storiesStatsCount.text = spanTextColors(textListStories[0],textListStories[1])

                        eventsStatsBar.visibility = View.GONE
                    }
                }
            }else{
                binding.scrollView.visibility = View.GONE
                binding.detailShimmerInclude.root.visibility = View.VISIBLE
                binding.detailShimmerInclude.shimmerDetailLayout.startShimmer()
            }
        })

        viewModel.creatorResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                binding.scrollView.visibility = View.VISIBLE
                binding.detailShimmerInclude.root.visibility = View.GONE
                binding.detailShimmerInclude.shimmerDetailLayout.stopShimmer()
                if(model.data.creators != null){
                    with(model.data.creators[0]){
                        var imageUrl = this.thumbnail?.path + "/portrait_uncanny." + this.thumbnail?.extension
                        glideWithListener(detailImage,imageUrl)

                        detailId.text = this.id.toString()
                        detailTitle.text = this.fullName
                        detailDescription.text = ""
                        rvTitle.text = "Events"
                        statsCharacter.text = "0"
                        statsEvents.text = this.events?.available.toString()
                        statsComics.text = this.comics?.available.toString()
                        statsSeries.text = this.series?.available.toString()
                        statsStories.text = this.stories?.available.toString()

                        val textListCharacters = createStatsBar(0)
                        characterStatsCount.text = spanTextColors(textListCharacters[0],textListCharacters[1])
                        val textListSeries = createStatsBar(this.series?.available)
                        seriesStatsCount.text = spanTextColors(textListSeries[0],textListSeries[1])
                        val textListComics = createStatsBar(this.comics?.available)
                        comicsStatsCount.text = spanTextColors(textListComics[0],textListComics[1])
                        val textListStories = createStatsBar(this.stories?.available)
                        storiesStatsCount.text = spanTextColors(textListStories[0],textListStories[1])
                        val textListEvents = createStatsBar(this.events?.available)
                        eventsStatsCount.text = spanTextColors(textListEvents[0],textListEvents[1])

                    }
                }
            }else{
                binding.scrollView.visibility = View.GONE
                binding.detailShimmerInclude.root.visibility = View.VISIBLE
                binding.detailShimmerInclude.shimmerDetailLayout.startShimmer()
            }
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
                var selectedCategory = args.category
                if(selectedCategory == Category.CHARACTERS){
                    fetchCharacterDetail(id)
                    fetchCharactersSeries(id)
                    binding.rvDetail.adapter = seriesAdapter
                }else if (selectedCategory == Category.SERIES){
                    fetchSeriesDetail(id)
                    fetchSeriesStories(id)
                    binding.rvDetail.adapter = storiesAdapter
                }
                else if (selectedCategory == Category.COMICS){
                    fetchComicDetail(id)
                    fetchComicsCreators(id)
                    binding.rvDetail.adapter = creatorsAdapter
                }
                else if (selectedCategory == Category.STORIES){
                    fetchStoriesDetail(id)
                    fetchStoriesComics(id)
                    binding.rvDetail.adapter = comicsAdapter
                }
                else if (selectedCategory == Category.EVENTS){
                    fetchEventDetail(id)
                    fetchEventsCharacters(id)
                    binding.rvDetail.adapter = characterAdapter
                }else if (selectedCategory == Category.CREATORS){
                    fetchCreatorDetail(id)
                    fetchCreatorsEvents(id)
                    binding.rvDetail.adapter = eventsAdapter
                }
            }
        }else{
            binding.scrollView.visibility = View.GONE
            binding.detailShimmerInclude.root.visibility = View.VISIBLE
            binding.detailShimmerInclude.shimmerDetailLayout.startShimmer()
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
                    binding.scrollView.visibility = View.VISIBLE
                    binding.detailShimmerInclude.root.visibility = View.GONE
                    binding.detailShimmerInclude.shimmerDetailLayout.stopShimmer()
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

    override fun onResume() {
        super.onResume()
        activity?.let {
            WindowCompat.setDecorFitsSystemWindows(it.window, false)
            val insetsController = WindowInsetsControllerCompat(it.window, it.window.decorView)
            insetsController.isAppearanceLightStatusBars = false
        }
    }

}