package com.example.moviearchitecturecomponents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.moviearchitecturecomponents.databinding.FragmentHomeBinding
import com.example.moviearchitecturecomponents.ui.slide.SlideAdapter
import com.example.moviearchitecturecomponents.util.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
//TODO https://www.behance.net/gallery/83595081/Photo-Play-UI-Kit-For-FREE REDESIGN APP

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.apply {
            homeViewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        val slides = SlideAdapter(context, MovieClickListener{ movie, imageView ->
            findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationMovie(
                    movie),
                    FragmentNavigatorExtras(imageView to movie.id.toString())
                )
        })
        val movies = PopularMoviesAdapter(context, MovieClickListener { movie, imageView ->
            findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationMovie(
                movie),
                FragmentNavigatorExtras(imageView to movie.id.toString())
                )
        })
        binding.moviesSlider.adapter = slides
        binding.moviesSlider.setPageTransformer(ZoomOutPageTransformer())
        binding.popularMovies.adapter = movies
        TabLayoutMediator(binding.indicator, binding.moviesSlider) { _, _ -> }.attach()

        homeViewModel.movies.observe(viewLifecycleOwner, {
            slides.dataSet = it.results?.subList(0, HomeViewModel.SPLIT_INDEX)!!
            movies.dataSet = it?.results.subList(HomeViewModel.SPLIT_INDEX, it?.results.size)
            (view.parent as? ViewGroup)?.doOnPreDraw {
                // Parent has been drawn. Start transitioning!
                startPostponedEnterTransition()
            }
        })

        homeViewModel.page.observe(viewLifecycleOwner, {
            binding.moviesSlider.setCurrentItem(it, true)
        })
    }

}

