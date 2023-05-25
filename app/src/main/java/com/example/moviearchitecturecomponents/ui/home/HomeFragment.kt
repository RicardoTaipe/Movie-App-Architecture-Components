package com.example.moviearchitecturecomponents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewGroupCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.moviearchitecturecomponents.databinding.FragmentHomeBinding
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.ui.home.MoviesAdapter.MoviesAdapterListener
import com.example.moviearchitecturecomponents.ui.home.slide.SlideAdapter
import com.example.moviearchitecturecomponents.ui.home.slide.SlideAdapter.SliderAdapterListener
import com.example.moviearchitecturecomponents.ui.movie.ApiStatus
import com.example.moviearchitecturecomponents.util.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment(), MoviesAdapterListener, SliderAdapterListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    private val slideAdapter = SlideAdapter(this)

    private val popularMoviesAdapter = MoviesAdapter(this)

    private val upcomingMoviesAdapter = MoviesAdapter(this)

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
        ViewGroupCompat.setTransitionGroup(binding.popularMovies as ViewGroup, true)
        ViewGroupCompat.setTransitionGroup(binding.upcomingMovies as ViewGroup, true)
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
            upcomingMovies.adapter = upcomingMoviesAdapter
            TabLayoutMediator(indicator, moviesSlider) { _, _ -> }.attach()
        }

        setupPopularMoviesObserver()
        setupUpcomingMoviesObserver()
        setupSliderObserver()
        setUpLoadingObserver()
    }

    private fun setUpLoadingObserver() {
        homeViewModel.status.observe(viewLifecycleOwner) {
            it ?: return@observe
            when (it) {
                ApiStatus.LOADING -> binding.apply {
                    loadingIndicator.visibility = View.VISIBLE
                    homeContainer.visibility = View.GONE
                }

                ApiStatus.DONE -> binding.apply {
                    loadingIndicator.visibility = View.GONE
                    homeContainer.visibility = View.VISIBLE
                }

                ApiStatus.ERROR -> binding.apply {
                    loadingIndicator.visibility = View.VISIBLE
                    homeContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun setupUpcomingMoviesObserver() {

        homeViewModel.upcomingMovies.observe(viewLifecycleOwner) {
            upcomingMoviesAdapter.submitList(it.results)
        }
    }

    private fun setupPopularMoviesObserver() {

        homeViewModel.movies.observe(viewLifecycleOwner) {
            slideAdapter.submitList(it.results?.subList(0, HomeViewModel.SPLIT_INDEX))
            popularMoviesAdapter.submitList(
                it?.results?.subList(HomeViewModel.SPLIT_INDEX, it.results.size)
            )
        }

    }

    private fun setupSliderObserver() {
        homeViewModel.page.observe(viewLifecycleOwner) {
            binding.moviesSlider.setCurrentItem(it, true)
        }
    }

    override fun onMovieClicked(movie: Result, imageView: ImageView) {
        moveToMoviePage(movie, imageView)

    }

    override fun onMovieSlideClicked(movie: Result, imageView: ImageView) {
        moveToMoviePage(movie, imageView)
    }

    private fun moveToMoviePage(movie: Result, imageView: ImageView) {
        findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToNavigationMovie(
                    movie
                ), FragmentNavigatorExtras(imageView to movie.id.toString())
            )
    }

}

