package com.example.moviearchitecturecomponents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewGroupCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.moviearchitecturecomponents.R
import com.example.moviearchitecturecomponents.databinding.FragmentHomeBinding
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.ui.home.PopularMoviesAdapter.PopularMoviesAdapterListener
import com.example.moviearchitecturecomponents.ui.home.slide.SlideAdapter
import com.example.moviearchitecturecomponents.ui.home.slide.SlideAdapter.SliderAdapterListener
import com.example.moviearchitecturecomponents.util.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialElevationScale

//TODO https://www.behance.net/gallery/83595081/Photo-Play-UI-Kit-For-FREE REDESIGN APP

class HomeFragment : Fragment(), PopularMoviesAdapterListener, SliderAdapterListener {

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private lateinit var binding: FragmentHomeBinding

    private val slideAdapter = SlideAdapter(this)

    private val popularMoviesAdapter = PopularMoviesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            homeViewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        ViewGroupCompat.setTransitionGroup(binding.root as ViewGroup, true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.run {
            moviesSlider.adapter = slideAdapter
            moviesSlider.setPageTransformer(ZoomOutPageTransformer())
            popularMovies.adapter = popularMoviesAdapter
            TabLayoutMediator(indicator, moviesSlider) { _, _ -> }.attach()
        }

        setupMovieSelectedObserver()
        setupSliderObserver()
    }

    private fun setupMovieSelectedObserver() {

        homeViewModel.movies.observe(viewLifecycleOwner, {
            slideAdapter.dataSet = it.results?.subList(0, HomeViewModel.SPLIT_INDEX)!!
            popularMoviesAdapter.dataSet =
                it?.results.subList(HomeViewModel.SPLIT_INDEX, it?.results.size)
        })

    }

    private fun setupSliderObserver() {
        homeViewModel.page.observe(viewLifecycleOwner, {
            binding.moviesSlider.setCurrentItem(it, true)
        })
    }

    override fun onMovieBannerClicked(movie: Result, imageView: ImageView) {

        applyTransitions()
        moveToMoviePage(movie, imageView)

    }

    override fun onMovieClicked(movie: Result, imageView: ImageView) {

        applyTransitions()
        moveToMoviePage(movie, imageView)

    }

    private fun moveToMoviePage(movie: Result, imageView: ImageView) {
        findNavController()
            .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationMovie(
                movie),
                FragmentNavigatorExtras(imageView to movie.id.toString())
            )
    }

    private fun applyTransitions() {

        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }

        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.material_motion_duration_long_1).toLong()
        }
    }

}

