package com.example.moviearchitecturecomponents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moviearchitecturecomponents.databinding.FragmentHomeBinding
import com.example.moviearchitecturecomponents.ui.slide.SlideAdapter
import com.example.moviearchitecturecomponents.util.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.apply {
            homeViewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        val slides = SlideAdapter(context)
        val movies = PopularMoviesAdapter(context)
        binding.moviesSlider.adapter = slides
        binding.popularMovies.adapter = movies
        binding.moviesSlider.setPageTransformer(ZoomOutPageTransformer())
        TabLayoutMediator(binding.indicator, binding.moviesSlider) { _, _ -> }.attach()

        homeViewModel.movies.observe(viewLifecycleOwner, {
            slides.dataSet = it.results?.subList(0, HomeViewModel.SPLIT_INDEX)!!
            movies.dataSet = it.results.subList(HomeViewModel.SPLIT_INDEX, it.results.size)
        })

        homeViewModel.page.observe(viewLifecycleOwner, {
            binding.moviesSlider.setCurrentItem(it, true)
        })

        return binding.root
    }
}

