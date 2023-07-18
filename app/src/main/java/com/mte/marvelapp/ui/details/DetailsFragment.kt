package com.mte.marvelapp.ui.details

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mte.marvelapp.R
import com.mte.marvelapp.data.remote.model.detail.DetailModel
import com.mte.marvelapp.databinding.FragmentDetailsBinding
import com.mte.marvelapp.ui.details.adapter.DetailsAdapter
import com.mte.marvelapp.ui.details.adapter.listener.DetailsRecyclerClickListener
import com.mte.marvelapp.ui.home.HomeFragmentDirections
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import com.mte.marvelapp.ui.home.uistate.ComicsUiState
import com.mte.marvelapp.ui.home.uistate.EventsUiState
import com.mte.marvelapp.ui.home.uistate.SeriesUiState
import com.mte.marvelapp.ui.home.uistate.StoriesUiState
import com.mte.marvelapp.utils.extensions.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding : FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : DetailsViewModel by viewModels()

    private val args : DetailsFragmentArgs by navArgs()

    private lateinit var detailsAdapter: DetailsAdapter


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
        setupRecyclerView()
        sendApiRequests()
        observeEvents()
        changeToolbarColor()
    }

    private fun setupRecyclerView() = with(binding) {
        detailsAdapter = DetailsAdapter(object : DetailsRecyclerClickListener{
            override fun onDetailsRecyclerClick(detail: DetailModel) {
                val action = DetailsFragmentDirections.actionDetailsFragmentSelf(detail.id.toString(),detail.category)
                findNavController().navigate(action)
            }
        })

        rvDetail.adapter = detailsAdapter
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
                }
            }
        })

        viewModel.characterUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is CharacterUiState.Loading -> {
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is CharacterUiState.Success -> {
                    state.data?.let {
                        val detailList : MutableList<DetailModel> = mutableListOf()
                        for(detail in state.data){
                            val model = DetailModel(detail.id,detail.name,detail.thumbnail.path + "/portrait_xlarge." + detail.thumbnail.extension,"characters")
                            detailList.add(model)
                        }
                        pbRecycler.visibility = View.GONE
                        detailsAdapter.listDetail = detailList
                        rvDetail.visibility = View.VISIBLE
                    }
                }
                is CharacterUiState.Error -> {

                }
            }
        })

        viewModel.seriesUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is SeriesUiState.Loading -> {
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is SeriesUiState.Success -> {
                    state.data?.let {
                        val detailList : MutableList<DetailModel> = mutableListOf()
                        for(detail in state.data){
                            val model = DetailModel(detail.id,detail.title,detail.thumbnail.path + "/portrait_xlarge." + detail.thumbnail.extension,"series")
                            detailList.add(model)
                        }
                        pbRecycler.visibility = View.GONE
                        detailsAdapter.listDetail = detailList
                        rvDetail.visibility = View.VISIBLE
                    }
                }
                is SeriesUiState.Error -> {

                }
            }
        })

        viewModel.comicsUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is ComicsUiState.Loading -> {
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is ComicsUiState.Success -> {
                    state.data?.let {
                        val detailList : MutableList<DetailModel> = mutableListOf()
                        for(detail in state.data){
                            val model = DetailModel(detail.id,detail.title,detail.thumbnail.path + "/portrait_xlarge." + detail.thumbnail.extension,"comics")
                            detailList.add(model)
                        }
                        pbRecycler.visibility = View.GONE
                        detailsAdapter.listDetail = detailList
                        rvDetail.visibility = View.VISIBLE
                    }
                }
                is ComicsUiState.Error -> {

                }
            }
        })

        viewModel.storiesUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is StoriesUiState.Loading -> {
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is StoriesUiState.Success -> {
                    state.data?.let {
                        val detailList : MutableList<DetailModel> = mutableListOf()
                        for(detail in state.data){
                            val model = DetailModel(detail.id,detail.title,detail.thumbnail?.path + "/portrait_xlarge." + detail.thumbnail?.extension,"stories")
                            detailList.add(model)
                        }
                        pbRecycler.visibility = View.GONE
                        detailsAdapter.listDetail = detailList
                        rvDetail.visibility = View.VISIBLE
                    }
                }
                is StoriesUiState.Error -> {

                }
            }
        })

        viewModel.eventsUiState.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is EventsUiState.Loading -> {
                    pbRecycler.visibility = View.VISIBLE
                    rvDetail.visibility = View.GONE
                }
                is EventsUiState.Success -> {
                    state.data?.let {
                        val detailList : MutableList<DetailModel> = mutableListOf()
                        for(detail in state.data){
                            val model = DetailModel(detail.id,detail.title,detail.thumbnail.path + "/portrait_xlarge." + detail.thumbnail.extension,"events")
                            detailList.add(model)
                        }
                        pbRecycler.visibility = View.GONE
                        detailsAdapter.listDetail = detailList
                        rvDetail.visibility = View.VISIBLE
                    }
                }
                is EventsUiState.Error -> {

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
            }else if (selectedCategory == "series"){
                fetchSeriesDetail(id)
                fetchSeriesStories(id)
            }
            else if (selectedCategory == "comics"){
                fetchComicDetail(id)
            }
            else if (selectedCategory == "stories"){
                fetchStoriesDetail(id)
                fetchStoriesComics(id)
            }
            else if (selectedCategory == "events"){
                fetchEventDetail(id)
                fetchEventsCharacters(id)
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