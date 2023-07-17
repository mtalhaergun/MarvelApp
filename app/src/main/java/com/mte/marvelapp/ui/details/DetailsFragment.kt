package com.mte.marvelapp.ui.details

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.mte.marvelapp.ui.home.uistate.SeriesUiState
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

            }
        })

        rvDetail.adapter = detailsAdapter
    }

    private fun observeEvents() = with(binding){

        viewModel.characterResponse.observe(viewLifecycleOwner, Observer {model ->
            if (model != null) {
                with(model.data.characters[0]){
                    var characterImageUrl = this.thumbnail.path + "/portrait_uncanny." + this.thumbnail.extension
                    Glide.with(heroImage).load(characterImageUrl).listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            heroImage.setImageResource(R.drawable.image_not_available)
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

                    }).into(heroImage)

                    heroName.text = this.name
                    detailDescription.text = this.description
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
                            val model = DetailModel(detail.id,detail.title,detail.thumbnail.path + "/portrait_xlarge." + detail.thumbnail.extension)
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

        binding.iconBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun sendApiRequests() = with(viewModel){
        args.id?.let {id ->
            fetchCharacterDetail(id)
            fetchCharactersSeries(id)
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

}